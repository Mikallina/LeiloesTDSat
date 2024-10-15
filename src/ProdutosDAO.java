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
        try (Connection conn = conecta.connectDB(); PreparedStatement prep = conn.prepareStatement(sql)) {
            prep.setString(1, produto.getNome());
            prep.setInt(2, produto.getValor());
            prep.setString(3, produto.getStatus());
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + e.getMessage());
            return false;
        }
    }

    public boolean venderProduto(int idProduto) throws SQLException {
        String verificarSql = "SELECT status FROM produtos WHERE id = ?";
        String atualizarSql = "UPDATE produtos SET status = 'Vendido' WHERE id = ? AND status != 'Vendido'";

        try (Connection conn = conecta.connectDB(); PreparedStatement verificarPrep = conn.prepareStatement(verificarSql); PreparedStatement atualizarPrep = conn.prepareStatement(atualizarSql)) {

            // Verifica o status do produto
            verificarPrep.setInt(1, idProduto);
            ResultSet resultSet = verificarPrep.executeQuery();

            if (resultSet.next()) {
                String statusAtual = resultSet.getString("status");
                if (statusAtual.equals("Vendido")) {
                    return false; // Produto já vendido
                }
            } else {
                return false; // Produto não encontrado
            }

            // Atualiza o status para 'Vendido'
            atualizarPrep.setInt(1, idProduto);
            return atualizarPrep.executeUpdate() > 0; // Retorna verdadeiro se a atualização for bem-sucedida
        }
    }

    public ArrayList<ProdutosDTO> listarProdutosVendidos() {
        ArrayList<ProdutosDTO> listagem = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";
        try (Connection conn = conecta.connectDB(); PreparedStatement prep = conn.prepareStatement(sql); ResultSet resultSet = prep.executeQuery()) {

            while (resultSet.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultSet.getInt("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setValor(resultSet.getInt("valor"));
                produto.setStatus(resultSet.getString("status"));
                listagem.add(produto);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos Vendidos: " + e.getMessage());
        }
        return listagem;
    }

    public ArrayList<ProdutosDTO> listarProdutos() {
        ArrayList<ProdutosDTO> listagem = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (Connection conn = conecta.connectDB(); PreparedStatement prep = conn.prepareStatement(sql); ResultSet resultSet = prep.executeQuery()) {

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
