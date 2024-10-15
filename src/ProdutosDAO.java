/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException; 



public class ProdutosDAO {
    
    
    private conectaDAO conecta = new conectaDAO();
    
  public boolean cadastrarProduto(ProdutosDTO produto) {
    String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
    try (Connection conn = conecta.connectDB(); 
         PreparedStatement prep = conn.prepareStatement(sql)) {
        prep.setString(1, produto.getNome());
        prep.setInt(2, produto.getValor());
        prep.setString(3, produto.getStatus());
        return prep.executeUpdate() > 0;
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + e.getMessage());
        return false;
    }
}
  
  public boolean venderProduto(int IDProduto) throws SQLException {
	    String sql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
	    try (Connection conn = conecta.connectDB();
	         PreparedStatement prep = conn.prepareStatement(sql)) {
	        prep.setInt(1, IDProduto);
	        return prep.executeUpdate() > 0;
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Erro ao vender produto: " + e.getMessage());
	        return false;
	    }
	}

    public ArrayList<ProdutosDTO> listarProdutos() {
        ArrayList<ProdutosDTO> listagem = new ArrayList<>();
        String sql = "SELECT * FROM produtos"; 
        try (Connection conn = conecta.connectDB(); 
             PreparedStatement prep = conn.prepareStatement(sql);
             ResultSet resultSet = prep.executeQuery()) {

            while (resultSet.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultSet.getInt("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setValor(resultSet.getInt("valor"));
                produto.setStatus(resultSet.getString("status"));
                listagem.add(produto);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage());
        }
        
        return listagem;
    }
}
