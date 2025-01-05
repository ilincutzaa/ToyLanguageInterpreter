package interpreter.toylanguageinterpreter;

import interpreter.toylanguageinterpreter.Model.Statement.IStmt;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Service.Service;
import interpreter.toylanguageinterpreter.Utils.AtomicIntegerKey;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIStack;
import interpreter.toylanguageinterpreter.Wrappers.MyIStackWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Map;

public class ProgramStateGUIController {
    @FXML
    public TextField prgStatesNumberTxt;

    @FXML
    public TableView<Map.Entry<AtomicIntegerKey, Value>> heapTableView;
    @FXML
    public ListView<Value> outListView;
    @FXML
    public ListView<String> fileTableListView;

    @FXML
    public TableView symbolTableView;
    @FXML
    public ListView<Integer> prgStateIDsListView;
    @FXML
    public ListView<IStmt> exeStackListView;


    @FXML
    public Button runOneStepButton;


    private Service service;

    public void setService(Service service) {
        this.service = service;
    }

    private int currentProgramStateID;

    public int getCurrentProgramStateID() {
        return currentProgramStateID;
    }

    public void setCurrentProgramStateID(int currentProgramStateID) {
        this.currentProgramStateID = currentProgramStateID;
    }


    @FXML
    public void initialize() {
    }

    public void refreshGUI() {
        updatePrgStatesNumberTxt();
        exeStackListView.setItems(getExeStackList());
        prgStateIDsListView.setItems(getPrgStateIDsList());
    }

    @FXML
    protected void onRunOneStepButtonClick() {
        try {
            service.runOneStep();
            refreshGUI();
        } catch (MyException e) {
            runOneStepButton.setDisable(true);
        }
    }


    private ObservableList<IStmt> getExeStackList() {
        if (service.getPrgIds().contains(currentProgramStateID)) {
            MyIStack<IStmt> exeStk = service.getRepo().getPrgState(currentProgramStateID).getExeStack();
            MyIStackWrapper<IStmt> exeStackWrapper = new MyIStackWrapper<>(exeStk);
            return exeStackWrapper.getObservableStack();
        }
        return javafx.collections.FXCollections.observableArrayList();
    }

    private ObservableList<Integer> getPrgStateIDsList() {
        return javafx.collections.FXCollections.observableArrayList(service.getPrgIds());
    }

    protected void updatePrgStatesNumberTxt(){
        int size = service.getRepo().getSize();
        prgStatesNumberTxt.setText(String.valueOf(size));
    }
}
