package br.gov.mma.biometric.ui;

import br.gov.mma.biometric.*;
import br.gov.mma.biometric.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Aplicação principal com interface gráfica para o sistema de autenticação biométrica.
 * Interface moderna usando Swing com design limpo e intuitivo.
 */
public class BiometricAuthenticationApp extends JFrame {
    
    private final BiometricAuthenticator authenticator;
    private final BiometricScanner scanner;
    private final CryptoService cryptoService;
    
    private JTabbedPane tabbedPane;
    private JTextArea logArea;
    
    public BiometricAuthenticationApp() throws CryptoException {
        // Inicializar componentes do sistema
        this.cryptoService = new CryptoServiceImpl();
        UserDatabase userDatabase = new InMemoryUserDatabase(cryptoService);
        SessionManager sessionManager = new SessionManagerImpl();
        AuditLog auditLog = new AuditLogImpl();
        
        this.authenticator = new BiometricAuthenticatorImpl(
            userDatabase, sessionManager, auditLog, cryptoService
        );
        this.scanner = new FacialRecognitionScanner();
        
        // Configurar janela principal
        setTitle("Sistema de Autenticação Biométrica - Ministério do Meio Ambiente");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Aplicar tema moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Criar interface
        createUI();
        
        // Cadastrar usuários de exemplo
        cadastrarUsuariosExemplo();
    }
    
    private void createUI() {
        // Painel principal com layout BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 245));
        
        // Cabeçalho
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Abas principais
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        tabbedPane.addTab("🔐 Autenticação", createAuthenticationPanel());
        tabbedPane.addTab("👤 Cadastro", createEnrollmentPanel());
        tabbedPane.addTab("📊 Informações", createInfoPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Área de log
        JPanel logPanel = createLogPanel();
        mainPanel.add(logPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 102, 204));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Sistema de Autenticação Biométrica");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Ministério do Meio Ambiente - Controle de Acesso Estratégico");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        panel.add(textPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createAuthenticationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel titleLabel = new JLabel("Autenticação Facial");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Seleção de nível de acesso
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel levelLabel = new JLabel("Nível de Acesso:");
        levelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(levelLabel, gbc);
        
        gbc.gridx = 1;
        JComboBox<AccessLevel> levelCombo = new JComboBox<>(AccessLevel.values());
        levelCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(levelCombo, gbc);
        
        // Botão de autenticação
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton authButton = createStyledButton("🔍 Capturar e Autenticar", new Color(0, 153, 76));
        authButton.addActionListener(e -> realizarAutenticacao((AccessLevel) levelCombo.getSelectedItem()));
        panel.add(authButton, gbc);
        
        // Área de resultado
        gbc.gridy = 3;
        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, gbc);
        
        return panel;
    }
    
    private JPanel createEnrollmentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel titleLabel = new JLabel("Cadastro de Novo Usuário");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Campos de entrada
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("ID do Usuário:"), gbc);
        gbc.gridx = 1;
        JTextField userIdField = new JTextField(20);
        panel.add(userIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Cargo:"), gbc);
        gbc.gridx = 1;
        JTextField roleField = new JTextField(20);
        panel.add(roleField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Nível de Acesso:"), gbc);
        gbc.gridx = 1;
        JComboBox<AccessLevel> levelCombo = new JComboBox<>(AccessLevel.values());
        panel.add(levelCombo, gbc);
        
        // Botão de cadastro
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JButton enrollButton = createStyledButton("📸 Capturar Biometria e Cadastrar", new Color(0, 102, 204));
        enrollButton.addActionListener(e -> realizarCadastro(
            userIdField.getText(),
            nameField.getText(),
            roleField.getText(),
            (AccessLevel) levelCombo.getSelectedItem()
        ));
        panel.add(enrollButton, gbc);
        
        return panel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        
        String info = "SISTEMA DE AUTENTICAÇÃO BIOMÉTRICA\n" +
                     "Ministério do Meio Ambiente\n\n" +
                     "Este sistema controla o acesso a informações estratégicas sobre propriedades rurais\n" +
                     "que utilizam agrotóxicos proibidos.\n\n" +
                     "NÍVEIS DE ACESSO:\n\n" +
                     "• PÚBLICO (Nível 1)\n" +
                     "  Informações gerais acessíveis a todos os funcionários\n\n" +
                     "• RESTRITO (Nível 2)\n" +
                     "  Informações restritas aos diretores de divisões\n\n" +
                     "• CONFIDENCIAL (Nível 3)\n" +
                     "  Informações confidenciais acessíveis apenas ao Ministro\n\n" +
                     "TECNOLOGIA:\n\n" +
                     "• Reconhecimento Facial com threshold de 0.88\n" +
                     "• Criptografia AES-256-GCM para templates biométricos\n" +
                     "• Auditoria completa de todas as operações\n" +
                     "• Controle hierárquico de acesso\n\n" +
                     "USUÁRIOS DE EXEMPLO:\n\n" +
                     "• USER-001 (João Silva) - Funcionário Público - Nível PÚBLICO\n" +
                     "• DIR-001 (Maria Santos) - Diretora - Nível RESTRITO\n" +
                     "• MIN-001 (Carlos Oliveira) - Ministro - Nível CONFIDENCIAL";
        
        infoArea.setText(info);
        
        JScrollPane scrollPane = new JScrollPane(infoArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Log de Operações"));
        
        logArea = new JTextArea(5, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(logArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(300, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void realizarAutenticacao(AccessLevel requestedLevel) {
        try {
            log("Iniciando captura biométrica facial...");
            
            // Capturar biometria
            BiometricData biometricData = scanner.capture();
            log("Biometria capturada. Qualidade: " + String.format("%.2f", biometricData.getQuality()));
            
            // Autenticar
            AuthenticationResult result = authenticator.authenticate(biometricData, requestedLevel);
            
            // Exibir resultado
            if (result.isSuccess()) {
                String message = "✅ AUTENTICAÇÃO BEM-SUCEDIDA!\n\n" +
                               "Usuário: " + result.getUser().getName() + "\n" +
                               "ID: " + result.getUser().getUserId() + "\n" +
                               "Cargo: " + result.getUser().getRole() + "\n" +
                               "Nível Concedido: " + result.getGrantedLevel().getDescription() + "\n" +
                               "Token: " + result.getSessionToken().substring(0, 20) + "...\n" +
                               "Timestamp: " + result.getTimestamp();
                
                JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                log("Autenticação bem-sucedida: " + result.getUser().getName());
            } else {
                String message = "❌ AUTENTICAÇÃO FALHOU\n\n" +
                               "Motivo: " + result.getMessage();
                
                JOptionPane.showMessageDialog(this, message, "Falha", JOptionPane.ERROR_MESSAGE);
                log("Autenticação falhou: " + result.getMessage());
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            log("Erro durante autenticação: " + e.getMessage());
        }
    }
    
    private void realizarCadastro(String userId, String name, String role, AccessLevel accessLevel) {
        if (userId.isEmpty() || name.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            log("Iniciando cadastro de " + name + "...");
            
            // Capturar biometria
            BiometricData biometricData = scanner.capture();
            log("Biometria capturada. Qualidade: " + String.format("%.2f", biometricData.getQuality()));
            
            // Criar usuário
            User user = new User(userId, name, role, accessLevel, null, BiometricType.FACIAL_RECOGNITION, true);
            
            // Cadastrar
            boolean success = authenticator.enrollUser(user, biometricData);
            
            if (success) {
                String message = "✅ CADASTRO BEM-SUCEDIDO!\n\n" +
                               "Usuário: " + name + "\n" +
                               "ID: " + userId + "\n" +
                               "Nível de Acesso: " + accessLevel.getDescription();
                
                JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                log("Cadastro bem-sucedido: " + name);
            } else {
                JOptionPane.showMessageDialog(this, "Falha no cadastro. Verifique se o usuário já existe.", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                log("Falha no cadastro de " + name);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            log("Erro durante cadastro: " + e.getMessage());
        }
    }
    
    private void cadastrarUsuariosExemplo() {
      
    }
    
    private void log(String message) {
        logArea.append("[" + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                BiometricAuthenticationApp app = new BiometricAuthenticationApp();
                app.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao iniciar aplicação: " + e.getMessage(), 
                                            "Erro Fatal", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
