package br.gov.mma.biometric.ui;

import br.gov.mma.biometric.*;
import br.gov.mma.biometric.model.*;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Aplicação principal com interface gráfica e suporte a webcam real.
 * Interface moderna usando Swing com design limpo e intuitivo.
 */
public class BiometricAuthenticationAppWithWebcam extends JFrame {
    
    private final BiometricAuthenticator authenticator;
    private BiometricScanner scanner;
    private final CryptoService cryptoService;
    private boolean useWebcam;
    private Webcam webcam;
    private WebcamPanel webcamPanel;
    private JPanel webcamContainer;
    
    private JTabbedPane tabbedPane;
    private JTextArea logArea;
    private JCheckBox webcamCheckbox;
    
    public BiometricAuthenticationAppWithWebcam() throws CryptoException {
        // Inicializar componentes do sistema
        this.cryptoService = new CryptoServiceImpl();
        UserDatabase userDatabase = new InMemoryUserDatabase(cryptoService);
        SessionManager sessionManager = new SessionManagerImpl();
        AuditLog auditLog = new AuditLogImpl();
        
        this.authenticator = new BiometricAuthenticatorImpl(
            userDatabase, sessionManager, auditLog, cryptoService
        );
        
        // Sempre usar webcam (sem modo simulação)
        this.useWebcam = true;
        this.scanner = new WebcamFacialScanner();
        log("Sistema iniciado com captura via webcam.");
        
        // Configurar janela principal
        setTitle("Sistema de Autenticação Biométrica - Ministério do Meio Ambiente");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Adicionar listener para fechar webcam ao sair
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (webcamPanel != null) {
                    webcamPanel.stop();
                }
                if (webcam != null && webcam.isOpen()) {
                    webcam.close();
                }
                if (scanner instanceof WebcamFacialScanner) {
                    ((WebcamFacialScanner) scanner).close();
                }
            }
        });
        
        // Aplicar tema moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Inicializar preview da webcam
        initializeWebcamPreview();
        
        // Criar interface
        createUI();
        
        // Cadastrar usuários de exemplo
        cadastrarUsuariosExemplo();
    }
    
    /**
     * Inicializa o preview da webcam uma única vez para ser compartilhado entre abas.
     */
    private void initializeWebcamPreview() {
        try {
            webcam = Webcam.getDefault();
            if (webcam != null) {
                webcam.setViewSize(WebcamResolution.VGA.getSize());
                webcamPanel = new WebcamPanel(webcam);
                webcamPanel.setPreferredSize(new Dimension(640, 480));
                webcamPanel.setFPSDisplayed(true);
                webcamPanel.setMirrored(true);
                
                webcamContainer = new JPanel(new BorderLayout());
                webcamContainer.setBorder(BorderFactory.createTitledBorder("Preview da Webcam"));
                webcamContainer.add(webcamPanel, BorderLayout.CENTER);
                
                log("Preview da webcam inicializado");
            }
        } catch (Exception e) {
            log("Erro ao inicializar preview: " + e.getMessage());
            e.printStackTrace();
        }
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
        
        tabbedPane.addTab("Autenticação", createAuthenticationPanel());
        tabbedPane.addTab("Cadastro", createEnrollmentPanel());
        tabbedPane.addTab("Informações", createInfoPanel());
        
        // Adicionar listener para mover o preview da webcam entre abas
        if (useWebcam && webcamContainer != null) {
            tabbedPane.addChangeListener(e -> {
                // Remover o webcamContainer da aba anterior
                int selectedIndex = tabbedPane.getSelectedIndex();
                
                // Adicionar à aba atual (0 = Autenticação, 1 = Cadastro)
                if (selectedIndex == 0 || selectedIndex == 1) {
                    JPanel currentPanel = (JPanel) tabbedPane.getComponentAt(selectedIndex);
                    
                    // Remover de todas as abas primeiro
                    for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                        Component comp = tabbedPane.getComponentAt(i);
                        if (comp instanceof JPanel) {
                            ((JPanel) comp).remove(webcamContainer);
                        }
                    }
                    
                    // Adicionar à aba atual na posição correta
                    if (currentPanel.getLayout() instanceof GridBagLayout) {
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.gridx = 0;
                        gbc.gridy = selectedIndex == 0 ? 2 : 1; // Posição diferente em cada aba
                        gbc.gridwidth = 2;
                        gbc.fill = GridBagConstraints.BOTH;
                        gbc.weightx = 1.0;
                        gbc.weighty = selectedIndex == 0 ? 1.0 : 0.6;
                        gbc.insets = new Insets(10, 10, 10, 10);
                        
                        currentPanel.add(webcamContainer, gbc, 2); // Adicionar na posição 2
                        currentPanel.revalidate();
                        currentPanel.repaint();
                    }
                }
            });
            
            // Adicionar à primeira aba inicialmente
            SwingUtilities.invokeLater(() -> {
                tabbedPane.setSelectedIndex(0);
                tabbedPane.getChangeListeners()[0].stateChanged(new javax.swing.event.ChangeEvent(tabbedPane));
            });
        }
        
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
        
        JLabel subtitleLabel = new JLabel("Ministério do Meio Ambiente");
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
        
        // Espaço reservado para o preview da webcam (será adicionado dinamicamente)
        // A posição gbc.gridy = 2 está reservada para o webcamContainer
        
        // Seleção de nível de acesso
        gbc.gridy = 3;
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
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton authButton = createStyledButton("Capturar e Autenticar", new Color(0, 153, 76));
        authButton.addActionListener(e -> realizarAutenticacao((AccessLevel) levelCombo.getSelectedItem()));
        panel.add(authButton, gbc);
        
        // Área de resultado
        gbc.gridy = 5;
        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, gbc);
        
        return panel;
    }
    
    private void toggleCaptureMode() {
        try {
            // Parar e fechar preview da webcam se estiver ativo
            if (webcamPanel != null) {
                webcamPanel.stop();
                webcamPanel = null;
            }
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
                webcam = null;
            }
            webcamContainer = null;
            
            // Fechar scanner atual se for webcam
            if (scanner instanceof WebcamFacialScanner) {
                ((WebcamFacialScanner) scanner).close();
            }
            
            // Alternar modo
            useWebcam = !useWebcam;
            
            // Criar novo scanner
            if (useWebcam && WebcamFacialScanner.isWebcamAvailable()) {
                scanner = new WebcamFacialScanner();
                initializeWebcamPreview(); // Reinicializar preview
                log("Alternado para modo WEBCAM REAL");
            } else {
                scanner = new FacialRecognitionScanner();
                useWebcam = false;
                log("Alternado para modo SIMULAÇÃO");
            }
            
            // Recriar interface
            getContentPane().removeAll();
            createUI();
            revalidate();
            repaint();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao alternar modo: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            log("Erro ao alternar modo: " + e.getMessage());
        }
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
        
        // Espaço reservado para o preview da webcam (será adicionado dinamicamente)
        // A posição gbc.gridy = 1 está reservada para o webcamContainer
        
        // Campos de entrada
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("ID do Usuário:"), gbc);
        gbc.gridx = 1;
        JTextField userIdField = new JTextField(20);
        panel.add(userIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Cargo:"), gbc);
        gbc.gridx = 1;
        JTextField roleField = new JTextField(20);
        panel.add(roleField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Nível de Acesso:"), gbc);
        gbc.gridx = 1;
        JComboBox<AccessLevel> levelCombo = new JComboBox<>(AccessLevel.values());
        panel.add(levelCombo, gbc);
        
        // Botão de cadastro
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JButton enrollButton = createStyledButton("Capturar e Cadastrar", new Color(0, 102, 204));
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
        button.setPreferredSize(new Dimension(350, 45));
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
            log("Preparando captura biométrica...");
            JOptionPane.showMessageDialog(this, 
                "Posicione seu rosto na frente da webcam.\nClique em OK quando estiver pronto.", 
                "Captura Facial", JOptionPane.INFORMATION_MESSAGE);
            
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
            log("Preparando captura biométrica para cadastro...");
            JOptionPane.showMessageDialog(this, 
                "Posicione seu rosto na frente da webcam.\nClique em OK quando estiver pronto.", 
                "Captura Facial", JOptionPane.INFORMATION_MESSAGE);
            
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
        try {
            // Usuário 1: Funcionário Público
            User user1 = new User("USER-001", "João Silva", "Funcionário Público", 
                                 AccessLevel.PUBLIC, null, BiometricType.FACIAL_RECOGNITION, true);
            BiometricData bio1 = new BiometricData(new byte[512], BiometricType.FACIAL_RECOGNITION, 0.95);
            new java.util.Random(12345).nextBytes(bio1.getTemplate());
            authenticator.enrollUser(user1, bio1);
            
            // Usuário 2: Diretora
            User user2 = new User("DIR-001", "Maria Santos", "Diretora de Divisão", 
                                 AccessLevel.RESTRICTED, null, BiometricType.FACIAL_RECOGNITION, true);
            BiometricData bio2 = new BiometricData(new byte[512], BiometricType.FACIAL_RECOGNITION, 0.92);
            new java.util.Random(67890).nextBytes(bio2.getTemplate());
            authenticator.enrollUser(user2, bio2);
            
            // Usuário 3: Ministro
            User user3 = new User("MIN-001", "Carlos Oliveira", "Ministro do Meio Ambiente", 
                                 AccessLevel.CONFIDENTIAL, null, BiometricType.FACIAL_RECOGNITION, true);
            BiometricData bio3 = new BiometricData(new byte[512], BiometricType.FACIAL_RECOGNITION, 0.98);
            new java.util.Random(11111).nextBytes(bio3.getTemplate());
            authenticator.enrollUser(user3, bio3);
            
            log("Usuários de exemplo cadastrados com sucesso");
            
        } catch (Exception e) {
            log("Erro ao cadastrar usuários de exemplo: " + e.getMessage());
        }
    }
    
    private void log(String message) {
        if (logArea != null) {
            logArea.append("[" + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                BiometricAuthenticationAppWithWebcam app = new BiometricAuthenticationAppWithWebcam();
                app.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao iniciar aplicação: " + e.getMessage(), 
                                            "Erro Fatal", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
