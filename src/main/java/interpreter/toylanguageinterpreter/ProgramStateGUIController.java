package interpreter.toylanguageinterpreter;

import interpreter.toylanguageinterpreter.Model.Statement.IStmt;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Service.Service;
import interpreter.toylanguageinterpreter.Utils.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
    public TableColumn<Map.Entry<AtomicIntegerKey, Value>, String> heapAddressColumn;
    @FXML
    public TableColumn<Map.Entry<AtomicIntegerKey, Value>, String> heapValueColumn;


    @FXML
    public ListView<Value> outListView;
    @FXML
    public ListView<String> fileTableListView;


    @FXML
    public TableView<Map.Entry<String, Value>> symbolTableView;
    @FXML
    public TableColumn<Map.Entry<String, Value>, String> symbolNameColumn;
    @FXML
    public TableColumn<Map.Entry<String, Value>, String> symbolValueColumn;


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

    public void setCurrentProgramStateID(int currentProgramStateID) {
        this.currentProgramStateID = currentProgramStateID;
    }

    private final ObservableList<IStmt> exeStackListData = FXCollections.observableArrayList();
    private final ObservableList<Map.Entry<String, Value>> symbolTableData = FXCollections.observableArrayList();
    private final ObservableList<Integer> prgStateIDsListData = FXCollections.observableArrayList();
    private final ObservableList<Value> outListData = FXCollections.observableArrayList();
    private final ObservableList<String> fileTableListData = FXCollections.observableArrayList();
    private final ObservableList<Map.Entry<AtomicIntegerKey, Value>> heapTableData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        prgStateIDsListView.setItems(prgStateIDsListData);
        exeStackListView.setItems(exeStackListData);
        outListView.setItems(outListData);
        fileTableListView.setItems(fileTableListData);
        heapTableView.setItems(heapTableData);
        symbolTableView.setItems(symbolTableData);
        initializeHeapTableView();
        initializeSymbolTableView();

        prgStateIDsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setCurrentProgramStateID(newValue);
                refreshGUIMini();
            }
        });
    }

    private void refreshGUIMini() {
        try {
            refreshExeStackList();
        } catch (MyException e) {
            if(!e.getMessage().equals("Program is finished"))
                e.printStackTrace();
        }
        try {
            refreshSymbolTable();
        } catch (MyException e) {
            if(!e.getMessage().equals("Program is finished"))
                e.printStackTrace();
        }
    }

    public void refreshGUI() {
        refreshPrgStatesNumberTxt();
        try {
            refreshExeStackList();
        } catch (MyException e) {
            if(!e.getMessage().equals("Program is finished"))
                e.printStackTrace();
        }
        refreshPrgStateIDsList();
        refreshOutList();
        refreshFileTableList();
        refreshHeapTable();
        try {
            refreshSymbolTable();
        } catch (MyException e) {
            if(!e.getMessage().equals("Program is finished"))
                e.printStackTrace();
        }
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


    private void refreshExeStackList() throws MyException {
        if (service.getPrgIds().contains(currentProgramStateID)) {
            MyIStack<IStmt> exeStk = service.getPrgState(currentProgramStateID).getExeStack();
            exeStackListData.setAll(exeStk.getInOrderStmts());
        }
        else
            throw new MyException("Program is finished");
    }

    private void refreshPrgStateIDsList() {
        prgStateIDsListData.setAll(service.getPrgIds());
    }

    private void refreshOutList() {
        outListData.setAll(service.getOutList().getList());
    }

    private void refreshFileTableList() {
        fileTableListData.setAll(service.getFilesKeys());
    }

    protected void refreshPrgStatesNumberTxt(){
        int size = service.getRepo().getSize();
        prgStatesNumberTxt.setText(String.valueOf(size));
    }

    private void initializeHeapTableView(){
        heapAddressColumn.setCellValueFactory(cellData ->{
            AtomicIntegerKey key = cellData.getValue().getKey();
            return new SimpleStringProperty(String.valueOf(key.get()));
        });

        heapValueColumn.setCellValueFactory(cellData -> {
            Value value = cellData.getValue().getValue();
            return new SimpleStringProperty(value.toString());
        });
    }

    private void initializeSymbolTableView(){
        symbolNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        symbolValueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().toString()));
    }

    private void refreshHeapTable() {
        heapTableData.setAll(service.getHeapEntry());
    }

    private void refreshSymbolTable() throws MyException {
        if (service.getPrgIds().contains(currentProgramStateID)) {
            MyIDictionary<String, Value> symTbl = service.getPrgState(currentProgramStateID).getSymTable();
            symbolTableData.setAll(symTbl.entrySet());
        }
        else
            throw new MyException("Program is finished");
    }
}
