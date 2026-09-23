import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FinanciamentoAppModificado extends JFrame {

    private static final double TAXA_JUROS = 0.10; // 10% fixo
    private static final Color COR_PRIMARIA = new Color(30, 60, 114);
    private static final Color COR_FUNDO = new Color(245, 247, 250);

    // Componentes de Entrada
    private JComboBox<String> cbMarca;
    private JTextField txtModelo, txtValor, txtQuilometragem, txtProprietarios, txtEntrada;
    private JComboBox<Integer> cbAno, cbParcelas;
    private JRadioButton rbNovo, rbUsado;
    private JCheckBox chkPossuiEntrada;

    // Componentes de Resultado
    private JLabel lblValorFinanciado, lblValorParcela, lblTotalPagar;

    public FinanciamentoAppModificado() {
        super("Simulador de Veículos Premium");
        configurarJanelaPrincipal();
        inicializarComponentes();
        montarInterface();
        configurarEventos();
    }

    private void configurarJanelaPrincipal() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COR_FUNDO);
    }

    private void inicializarComponentes() {
        cbMarca = new JComboBox<>(new String[]{"Selecione", "Chevrolet", "Fiat", "Ford", "Honda", "Hyundai", "Jeep", "Nissan", "Renault", "Toyota", "Volkswagen", "BMW", "Mercedes"});
        txtModelo = new JTextField();
        
        Integer[] anos = new Integer[27];
        for (int i = 0; i < 27; i++) anos[i] = 2026 - i;
        cbAno = new JComboBox<>(anos);
        cbAno.setSelectedIndex(-1);
        
        txtValor = new JTextField();
        
        rbNovo = new JRadioButton("0km (Novo)", true);
        rbUsado = new JRadioButton("Seminovo/Usado");
        ButtonGroup bgTipo = new ButtonGroup();
        bgTipo.add(rbNovo); bgTipo.add(rbUsado);
        rbNovo.setBackground(Color.WHITE);
        rbUsado.setBackground(Color.WHITE);

        txtQuilometragem = new JTextField();
        txtProprietarios = new JTextField();
        
        chkPossuiEntrada = new JCheckBox("Desejo dar um valor de entrada");
        chkPossuiEntrada.setBackground(Color.WHITE);
        txtEntrada = new JTextField();
        
        cbParcelas = new JComboBox<>(new Integer[]{12, 24, 36, 48, 60});
        cbParcelas.setSelectedIndex(-1);

        // Ajustando estado inicial dos campos condicionais
        txtQuilometragem.setEnabled(false);
        txtProprietarios.setEnabled(false);
        txtEntrada.setEnabled(false);
    }

    private void montarInterface() {
        // Cabeçalho
        JPanel painelHeader = new JPanel();
        painelHeader.setBackground(COR_PRIMARIA);
        painelHeader.setBorder(new EmptyBorder(15, 0, 15, 0));
        JLabel lblTitulo = new JLabel("SIMULADOR DE FINANCIAMENTO AUTOMOTIVO");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        painelHeader.add(lblTitulo);
        add(painelHeader, BorderLayout.NORTH);

        // Corpo Principal dividido em 2 colunas
        JPanel painelCorpo = new JPanel(new GridLayout(1, 2, 15, 15));
        painelCorpo.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelCorpo.setBackground(COR_FUNDO);

        painelCorpo.add(montarPainelFormulario());
        painelCorpo.add(montarPainelResultados());

        add(painelCorpo, BorderLayout.CENTER);
    }

    private JPanel montarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                " Preencha os Dados do Veículo ", TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14), COR_PRIMARIA));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.weightx = 1.0;

        int linha = 0;
        adicionarCampoGrid(painel, "Marca:", cbMarca, gbc, linha++);
        adicionarCampoGrid(painel, "Modelo:", txtModelo, gbc, linha++);
        adicionarCampoGrid(painel, "Ano do Veículo:", cbAno, gbc, linha++);
        adicionarCampoGrid(painel, "Valor Total (R$):", txtValor, gbc, linha++);

        // Tipo do veículo
        gbc.gridx = 0; gbc.gridy = linha++; gbc.gridwidth = 2;
        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTipo.setBackground(Color.WHITE);
        painelTipo.add(rbNovo); painelTipo.add(rbUsado);
        painel.add(painelTipo, gbc);

        // Campos de Usado (na mesma linha)
        gbc.gridy = linha++; gbc.gridwidth = 1; gbc.gridx = 0;
        painel.add(new JLabel("Km:"), gbc);
        gbc.gridx = 1; painel.add(txtQuilometragem, gbc);
        
        gbc.gridy = linha++; gbc.gridx = 0;
        painel.add(new JLabel("Proprietários:"), gbc);
        gbc.gridx = 1; painel.add(txtProprietarios, gbc);

        // Financiamento
        gbc.gridy = linha++; gbc.gridx = 0; gbc.gridwidth = 2;
        painel.add(new JSeparator(), gbc);

        gbc.gridy = linha++;
        painel.add(chkPossuiEntrada, gbc);

        gbc.gridy = linha++; gbc.gridwidth = 1; gbc.gridx = 0;
        painel.add(new JLabel("Valor Entrada (R$):"), gbc);
        gbc.gridx = 1; painel.add(txtEntrada, gbc);

        gbc.gridy = linha; gbc.gridx = 0;
        painel.add(new JLabel("Nº Parcelas:"), gbc);
        gbc.gridx = 1; painel.add(cbParcelas, gbc);

        return painel;
    }

    private void adicionarCampoGrid(JPanel painel, String rotulo, JComponent componente, GridBagConstraints gbc, int linha) {
        gbc.gridy = linha;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        painel.add(new JLabel(rotulo), gbc);
        gbc.gridx = 1;
        painel.add(componente, gbc);
    }

    private JPanel montarPainelResultados() {
        JPanel painelBase = new JPanel(new BorderLayout());
        painelBase.setBackground(COR_FUNDO);

        // Painel onde ficam os valores
        JPanel painelCards = new JPanel(new GridLayout(3, 1, 10, 10));
        painelCards.setBackground(COR_FUNDO);

        lblValorFinanciado = criarLabelResultado("Valor Financiado:");
        lblValorParcela = criarLabelResultado("Valor da Parcela:");
        lblTotalPagar = criarLabelResultado("Total a Pagar (C/ Juros):");
        lblTotalPagar.setForeground(new Color(46, 204, 113)); // Verde destaque

        painelCards.add(lblValorFinanciado);
        painelCards.add(lblValorParcela);
        painelCards.add(lblTotalPagar);

        // Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.setBackground(COR_FUNDO);

        JButton btnLimpar = new JButton("Limpar Dados");
        JButton btnCalcular = new JButton("SIMULAR AGORA");
        btnCalcular.setBackground(COR_PRIMARIA);
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setFont(new Font("Arial", Font.BOLD, 14));
        btnCalcular.setPreferredSize(new Dimension(180, 40));

        btnLimpar.addActionListener(e -> limparFormulario());
        btnCalcular.addActionListener(e -> processarCalculo());

        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnCalcular);

        painelBase.add(painelCards, BorderLayout.CENTER);
        painelBase.add(painelBotoes, BorderLayout.SOUTH);

        return painelBase;
    }

    private JLabel criarLabelResultado(String titulo) {
        JLabel label = new JLabel("<html><span style='font-size:12px; color:gray;'>" + titulo + "</span><br/><span style='font-size:24px;'>R$ 0,00</span></html>", SwingConstants.CENTER);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(15, 10, 15, 10)));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        return label;
    }

    private void configurarEventos() {
        rbNovo.addActionListener(e -> alternarCamposUsado(false));
        rbUsado.addActionListener(e -> alternarCamposUsado(true));
        
        chkPossuiEntrada.addActionListener(e -> {
            txtEntrada.setEnabled(chkPossuiEntrada.isSelected());
            if (!chkPossuiEntrada.isSelected()) txtEntrada.setText("");
        });
    }

    private void alternarCamposUsado(boolean habilitar) {
        txtQuilometragem.setEnabled(habilitar);
        txtProprietarios.setEnabled(habilitar);
        if (!habilitar) {
            txtQuilometragem.setText("");
            txtProprietarios.setText("");
        }
    }

    private void processarCalculo() {
        List<String> erros = validarDados();

        if (!erros.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", erros), "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Se chegou aqui, os dados são válidos
        try {
            double valorVeiculo = parseDouble(txtValor.getText());
            double valorEntrada = chkPossuiEntrada.isSelected() ? parseDouble(txtEntrada.getText()) : 0;
            int parcelas = (Integer) cbParcelas.getSelectedItem();

            double financiado = valorVeiculo - valorEntrada;
            double totalAPagar = financiado * (1 + TAXA_JUROS);
            double valorDaParcela = totalAPagar / parcelas;

            exibirResultados(financiado, valorDaParcela, totalAPagar, parcelas);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro inesperado ao calcular. Verifique os números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<String> validarDados() {
        List<String> erros = new ArrayList<>();

        if (cbMarca.getSelectedIndex() <= 0) erros.add("• Selecione a marca.");
        if (txtModelo.getText().trim().isEmpty()) erros.add("• Informe o modelo.");
        if (cbAno.getSelectedIndex() == -1) erros.add("• Selecione o ano.");
        
        double valorVeiculo = 0;
        try {
            valorVeiculo = parseDouble(txtValor.getText());
            if (valorVeiculo <= 0) erros.add("• O valor do veículo deve ser maior que zero.");
        } catch (Exception e) {
            erros.add("• Valor do veículo inválido.");
        }

        if (rbUsado.isSelected()) {
            try {
                double km = parseDouble(txtQuilometragem.getText());
                if (km < 0) erros.add("• A quilometragem não pode ser negativa.");
            } catch (Exception e) { erros.add("• Informe uma quilometragem válida."); }

            try {
                int prop = Integer.parseInt(txtProprietarios.getText().trim());
                if (prop < 1) erros.add("• O veículo deve ter tido pelo menos 1 proprietário.");
            } catch (Exception e) { erros.add("• Número de proprietários inválido."); }
        }

        if (chkPossuiEntrada.isSelected()) {
            try {
                double entrada = parseDouble(txtEntrada.getText());
                if (entrada <= 0) erros.add("• O valor de entrada deve ser maior que zero.");
                if (entrada >= valorVeiculo && valorVeiculo > 0) erros.add("• A entrada deve ser menor que o valor do veículo.");
            } catch (Exception e) { erros.add("• Valor de entrada inválido."); }
        }

        if (cbParcelas.getSelectedIndex() == -1) erros.add("• Selecione as parcelas.");

        return erros;
    }

    private void exibirResultados(double financiado, double parcela, double total, int numParcelas) {
        NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        
        lblValorFinanciado.setText(String.format("<html><span style='font-size:12px; color:gray;'>Valor Financiado:</span><br/><span style='font-size:24px; color:#2c3e50;'>%s</span></html>", moeda.format(financiado)));
        lblValorParcela.setText(String.format("<html><span style='font-size:12px; color:gray;'>Valor da Parcela (%dx):</span><br/><span style='font-size:24px; color:#2c3e50;'>%s</span></html>", numParcelas, moeda.format(parcela)));
        lblTotalPagar.setText(String.format("<html><span style='font-size:12px; color:gray;'>Total a Pagar:</span><br/><span style='font-size:24px; color:#27ae60;'><b>%s</b></span></html>", moeda.format(total)));
    }

    private void limparFormulario() {
        cbMarca.setSelectedIndex(0);
        txtModelo.setText("");
        cbAno.setSelectedIndex(-1);
        txtValor.setText("");
        rbNovo.setSelected(true);
        alternarCamposUsado(false);
        chkPossuiEntrada.setSelected(false);
        txtEntrada.setEnabled(false);
        txtEntrada.setText("");
        cbParcelas.setSelectedIndex(-1);
        exibirResultados(0, 0, 0, 0);
    }

    private double parseDouble(String texto) {
        if (texto == null || texto.trim().isEmpty()) throw new NumberFormatException();
        String formatado = texto.replace("R$", "").replace(" ", "").trim();
        if (formatado.contains(",")) {
            formatado = formatado.replace(".", "").replace(",", ".");
        }
        return Double.parseDouble(formatado);
    }

    public static void main(String[] args) {
        // Tenta aplicar um visual mais moderno nativo do sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new FinanciamentoAppModificado().setVisible(true);
        });
    }
}