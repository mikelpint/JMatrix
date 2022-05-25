package org.jmatrix.graphical;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.*;

import org.apfloat.Apfloat;
import org.apfloat.Apint;
import org.jmatrix.MatrixTypes.*;
import org.jmatrix.internal.JMath;
import org.jmatrix.internal.JMatrixIO;
import org.jmatrix.internal.Settings;

public class MainWindow extends Window implements ActionListener {
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;
    public static final String DEFAULT_TITLE = "JMatrix";
    public static final ImageIcon DEFAULT_ICON = new ImageIcon (Toolkit.getDefaultToolkit ().getImage ("src/org/jmatrix/graphical/logo.png"));

    protected JLabel logoImage;
    
    protected JButton generateMatricesButton;
    protected JButton loadMatrixButton;
    
    public MainWindow () {
        this (DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public MainWindow (int width, int height) {
        this (width, height, DEFAULT_TITLE);
    }

    public MainWindow (int width, int height, String title) {
        this (width, height, title, DEFAULT_ICON);
    }

    public MainWindow (int width, int height, String title, ImageIcon icon) {
        this (width, height, title, icon, DEFAULT_FONT);
    }

    public MainWindow (int width, int height, String title, ImageIcon icon, Font font) {
        this (width, height, title, icon, font, DEFAULT_FOREGROUND, DEFAULT_BACKGROUND);
    }

    public MainWindow (int width, int height, String title, ImageIcon icon, Font font, Color foreground, Color background) {
        super (width, height, title, icon, font, foreground, background);
        
        Container container = this.getContentPane ();
        
        JMenu settingsMenu = new JMenu ("Settings");
        JMenuItem styleMenu = new JMenuItem ("Style");
        styleMenu.addActionListener (
            new ActionListener () {
                @Override
                public void actionPerformed (ActionEvent e) {
                    String style = (String) JOptionPane.showInputDialog (
                        null,
                        "Select the style you want the matrices to be displayed on:",
                        null,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String [] {
                            "Array",
                            "List",
                            "Positional",
                            "Brackets",
                            "Braces",
                            "Parentheses",
                            "Pipes",
                            "Double pipes"
                        },
                        "Brackets"
                    );
                    
                    if (style == null) {
                        return;
                    }
                    
                    Settings.settings.setProperty ("Style", style);
                }
            }
        );
        settingsMenu.add (styleMenu);
        
        JMenuBar menuBar = new JMenuBar ();
        menuBar.add (settingsMenu);
        this.setJMenuBar (menuBar);
        
        this.logoImage = new JLabel (DEFAULT_ICON);
        
        this.loadMatrixButton = new JButton ("Load a matrix");
        this.loadMatrixButton.addActionListener (
            new ActionListener () {
                @Override
                public void actionPerformed (ActionEvent e) {
                    Matrix matrix = null;
                    
                    try {
                        JFileChooser chooseSaveLocation = new JFileChooser ();
                        chooseSaveLocation.showSaveDialog (container);
                        ObjectInputStream inputFile = new ObjectInputStream (new FileInputStream (chooseSaveLocation.getSelectedFile ()));
                        
                        matrix = (Matrix) inputFile.readObject ();
                    }
                    
                    catch (IOException ex) {
                        JOptionPane.showMessageDialog (
                            container,
                            "Could not load a matrix from the chosen file.",
                            null,
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                    
                    catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    
                    new MatrixPropertiesWindow (matrix);
                }
            }
        );
        
        this.generateMatricesButton = new JButton ("Generate a matrix");
        this.generateMatricesButton.addActionListener (this);
        this.generateMatricesButton.setActionCommand ("openGenerateMatricesWindow");

        JPanel mainPanel = new JPanel ();
        mainPanel.setLayout (new BorderLayout ((int) (this.width * 0.25), (int) (this.height * 0.25)));
        mainPanel.add (this.logoImage, BorderLayout.NORTH);
        mainPanel.add (this.loadMatrixButton, BorderLayout.WEST);
        mainPanel.add (this.generateMatricesButton, BorderLayout.EAST);
        
        container.setLayout (new BorderLayout ());
        container.add (mainPanel, BorderLayout.SOUTH);
        
        this.applyTheme ();
        this.setLocationRelativeTo (null);
        this.setVisible (true);
    }

    @Override
    public void applyTheme () {
        super.applyTheme ();

        Window.setUIFont (new FontUIResource (this.font));
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        Thread mainThread = new Thread (new Runnable () {
            @Override
            public void run () {
                String actionCommand = e.getActionCommand ();
        
                if (actionCommand.equals ("openGenerateMatricesWindow")) {
                    Matrix matrix = MatrixSelectorFactory.generateMatrix (
                        (String) JOptionPane.showInputDialog (
                            null,
                            "Which kind of matrix do you want to generate?",
                            null,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new String [] {
                                "Commutation matrix",
                                "Duplication matrix",
                                "Elimination matrix",
                                "Exchange matrix",
                                "Hilbert matrix",
                                "Identity matrix",
                                "Lehmer matrix",
                                "Lucas sequence-based matrix",
                                "Pascal matrix",
                                "Pauli matrix",
                                "Random matrix",
                                "Redheffer matrix",
                                "Scalar matrix",
                                "Shift matrix",
                                "Zero matrix"
                            },
                            "Identity matrix"
                        )
                    );
                    
                    if (matrix == null  || matrix.getMatrix () == null) {
                        return;
                    }
                    
                    if (
                        JOptionPane.showOptionDialog (
                            null,
                            "Do you want to save the matrix?",
                            null,
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new String [] {"Yes", "No"},
                            "Yes"
                        ) == JOptionPane.YES_OPTION
                    ) {
                        try {
                            JFileChooser chooseSaveLocation = new JFileChooser ();
                            chooseSaveLocation.showSaveDialog (null);
                            ObjectOutputStream outputFile = new ObjectOutputStream (new FileOutputStream (chooseSaveLocation.getSelectedFile ()));
                            
                            outputFile.writeObject (matrix);
                        }
                        
                        catch (IOException e) {
                            JOptionPane.showMessageDialog (
                                null,
                                "Could not save the generated matrix to the file.",
                                null,
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                    
                    new MatrixPropertiesWindow (matrix);
                }
            }
        });
        
        mainThread.start ();
    }

    public static void main (String [] args) throws IOException {
        new Settings ();
        
        MainWindow mainWindow = new MainWindow ();
    }
}
