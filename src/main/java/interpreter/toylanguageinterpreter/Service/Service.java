package interpreter.toylanguageinterpreter.Service;
import interpreter.toylanguageinterpreter.Model.Value.RefValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Repository.IRepository;
import interpreter.toylanguageinterpreter.Utils.*;
import interpreter.toylanguageinterpreter.Model.PrgState;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Service {

    private final IRepository<PrgState> repo;
    private ExecutorService executor;

    public Service(IRepository<PrgState> repo) {
        this.repo = repo;
        executor = Executors.newFixedThreadPool(2);
    }

    private List<PrgState> removeCompletedPrg(List<PrgState> inPrgList){
        return inPrgList.stream()
                .filter(p -> p.isNotCompleted())
                .collect(Collectors.toList());
    }

    private void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException, MyException {
        prgList.forEach(prg -> repo.logPrgStateExec(prg));
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (() -> p.oneStep()))
                .collect(Collectors.toList());
        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future->
                    {
                        try{
                        return future.get();
                        } catch (InterruptedException | ExecutionException | MyException e) {
                            throw new MyException(e.getMessage());
                        }
                    })
                .filter(p -> p != null)
                .collect(Collectors.toList());
        prgList.addAll(newPrgList);
        prgList.forEach(prg -> repo.logPrgStateExec(prg));
        repo.setPrgList(prgList);
    }

    public void allStep() throws MyException{
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        MyIDictionary<String, Value> globalSymTable = new MyDictionary<>();
        while (!prgList.isEmpty()) {
            //for gc: heap values are shared; symtbl have some common values before the fork, but ultimately are different =>
            //some heap values won't have reference in the current prgstate's symtbl, but they MUST NOT be erased UNLESS they dont have
            //reference in ALL the symtbls joined
            for(PrgState prg : prgList){
                globalSymTable.putAll(prg.getSymTable());
            }
            MyIHeap<Value> heap = prgList.getFirst().getHeapTable();
            var alladdr = getAllAddr(globalSymTable.values(), heap);
            heap.setContent(GarbageCollector(getAllAddr(globalSymTable.values(), heap), heap));
            try {
                oneStepForAllPrg(prgList);
            } catch (InterruptedException | MyException e) {
                throw new MyException(e.getMessage());
            }
            prgList = removeCompletedPrg(repo.getPrgList());
        }
        executor.shutdownNow();
        prgList = removeCompletedPrg(repo.getPrgList());

        //we are at the end of our program, run the garbage collector for one last time
        if(prgList.isEmpty() && repo.getSize() == 1) {
            globalSymTable.putAll(repo.getPrgList().getFirst().getSymTable());
            MyIHeap<Value> heap = repo.getPrgList().getFirst().getHeapTable();
            heap.setContent(GarbageCollector(getAllAddr(globalSymTable.values(), heap), heap));
            repo.logPrgStateExec(repo.getPrgList().getFirst());
        }

        repo.setPrgList(prgList);
    }

    Map<AtomicIntegerKey, Value> GarbageCollector(List<Integer> symTblAddr, MyIHeap<Value> heap){
        return heap.entrySet().stream()
                .filter(e->symTblAddr.contains(e.getKey().get()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Integer> getAllAddr(Collection<Value> symTblValues, MyIHeap<Value> heap){
        ConcurrentLinkedDeque<Integer> symTblAddr = symTblValues.stream()
                .filter(v->v instanceof RefValue)
                .map(v->{RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toCollection(ConcurrentLinkedDeque::new));

        symTblAddr.forEach(
                addr->{
                    Value val = heap.lookUp(new AtomicIntegerKey(addr));
                    if(val instanceof RefValue)
                        if(!symTblAddr.contains(((RefValue) val).getAddress()))
                            symTblAddr.add(((RefValue) val).getAddress());
                }
        );
        return symTblAddr.stream().toList();
    }

    public IRepository<PrgState> getRepo() {
        return repo;
    }

    public List<Integer> getPrgIds(){
        List<Integer> prgIds = new ArrayList<>();
        for(PrgState prg : repo.getPrgList()){
            prgIds.add(prg.getId());
        }
        return prgIds;
    }

    public void runOneStep() throws MyException {
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());

        if(prgList.isEmpty()) {
            executor.shutdownNow();
            throw new MyException("Program is finished");
        }
        MyIDictionary<String, Value> globalSymTable = new MyDictionary<>();

        for(PrgState prg : prgList)
            globalSymTable.putAll(prg.getSymTable());

        MyIHeap<Value> heap = prgList.getFirst().getHeapTable();
        heap.setContent(GarbageCollector(getAllAddr(globalSymTable.values(), heap), heap));

        try {
            oneStepForAllPrg(prgList);
        } catch (InterruptedException | MyException e) {
            throw new MyException(e.getMessage());
        }

        repo.setPrgList(prgList);
    }

    public MyIList<Value> getOutList() {
        return getRepo().getPrgList().getFirst().getOut();
    }

    public Set<String> getFilesKeys() {
        return getRepo().getPrgList().getFirst().getFileTable().keySet();
    }

    public Set<Map.Entry<AtomicIntegerKey, Value>> getHeapEntry() {
        return getRepo().getPrgList().getFirst().getHeapTable().entrySet();
    }

    public PrgState getPrgState(int id) throws MyException{
        for(PrgState prg: repo.getPrgList())
            if(prg.getId() == id)
                return prg;
        throw new MyException("No prg state with id " + id);
    }
}
