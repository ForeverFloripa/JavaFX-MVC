package javafxmvc.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.domain.Cliente;

public class DialogClientesController implements Initializable {

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtCpf;

	@FXML
	private TextField txtFone;

	@FXML
	private Button btnConfirmar;

	@FXML
	private Button btnCancelar;

	@FXML
	private Label lblNome;

	@FXML
	private Label lblCpf;

	@FXML
	private Label lblTelefone;

	private Stage dialogStage;
	private boolean buttonConfirmarClicked = false;
	private Cliente cliente;

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isButtonConfirmarClicked() {
		return buttonConfirmarClicked;
	}

	public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
		this.buttonConfirmarClicked = buttonConfirmarClicked;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@FXML
	public void handleButtonConfirmar() {

	//if (validarEntradaDeDados()) {

			cliente.setNome(txtNome.getText());
			cliente.setCpf(txtCpf.getText());
			cliente.setTelefone(txtFone.getText());

			buttonConfirmarClicked = true;
			dialogStage.close();
		//}

	}

	@FXML
	public void handleButtonCancelar() {
		dialogStage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
