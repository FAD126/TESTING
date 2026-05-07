package com.example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class InicioSesion extends JFrame implements ActionListener {

    private JLabel bienvenida, mensaje, labelNombre, labelContrasenia;
    private JTextField nombre;
    private JPasswordField contrasenia;
    private JButton ingresar;

    public InicioSesion() throws HeadlessException {
        super("Inicio de sesión");

        // Layout profesional y adaptable
        setLayout(new BorderLayout(0, 16));
        setResizable(false);
        // Al cerrar el login, cerrar toda la app
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Contenedor central
        JPanel center = new JPanel(new GridBagLayout());
        center.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        bienvenida = new JLabel("Registro de Notas");
        bienvenida.setFont(bienvenida.getFont().deriveFont(Font.BOLD, 28f));
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        center.add(bienvenida, gbc);

        mensaje = new JLabel("Ingrese sus datos para poder iniciar sesión:");
        mensaje.setFont(mensaje.getFont().deriveFont(16f));
        gbc.gridy = 1;
        center.add(mensaje, gbc);

        labelNombre = new JLabel("Nombre de usuario:");
        labelNombre.setFont(mensaje.getFont().deriveFont(16f));
        gbc.gridy = 2;
        center.add(labelNombre, gbc);

        nombre = new JTextField();
        nombre.setFont(nombre.getFont().deriveFont(18f));
        gbc.gridy = 3;
        gbc.weightx = 1;
        center.add(nombre, gbc);

        labelContrasenia = new JLabel("Contraseña:");
        labelContrasenia.setFont(mensaje.getFont().deriveFont(16f));
        gbc.gridy = 4;
        center.add(labelContrasenia, gbc);

        contrasenia = new JPasswordField();
        contrasenia.setFont(contrasenia.getFont().deriveFont(18f));
        gbc.gridy = 5;
        center.add(contrasenia, gbc);

        ingresar = new JButton("Ingresar");
        ingresar.setFont(ingresar.getFont().deriveFont(Font.BOLD, 18f));
        ingresar.setPreferredSize(new Dimension(180, 45));
        ingresar.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(ingresar);
        gbc.gridy = 6;
        gbc.weightx = 0;
        center.add(buttonPanel, gbc);

        add(center, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (ingresar == e.getSource()) {

            String nom = nombre.getText();
            String cont = contrasenia.getText();

            LoginService2 auth = new LoginService2();
            if (auth.authenticate(nom, cont)) {
                // Cerrar pantalla de login y abrir el sistema
                JOptionPane.showMessageDialog(this, "Registro exitoso");
                dispose();
                NotesAppFrame frame = new NotesAppFrame(new GradeRepository());
                frame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error al ingresar datos");
            }
        }
    }

    public static void main(String args[]) {
        InicioSesion inicioSesion = new InicioSesion();

        // Tamaño grande
        inicioSesion.setSize(900, 650);
        // Centrar en pantalla
        inicioSesion.setLocationRelativeTo(null);
        inicioSesion.setVisible(true);
    }

}
