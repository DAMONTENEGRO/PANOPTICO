
package panoptico.interfaz;

import java.awt.Font;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import panoptico.clases.Equipo;

/**
 * @author montenegro 
 * @author santamaria 
 * @author solano 
 **/
public class VentanaBienvenida extends JFrame implements ActionListener, Runnable{
    
    private final JButton boton1, boton2;
    private final JLabel titulo1, titulo2;
    private final Font fuente = new Font("TimesRoman", Font.BOLD, 65);
    private final Font fuente2 = new Font("TimesRoman", Font.PLAIN, 30);
    private final Font fuente3 = new Font("TimesRoman", Font.PLAIN, 20);
    
    public VentanaBienvenida() throws ParseException, IOException {
        
        // Botones
        
        boton1 = new JButton();
        boton1.setText("Salir");
        boton1.setFont(fuente3);
        boton1.setBounds(30, 330, 120, 30);
        boton1.addActionListener(this);
        
        boton2 = new JButton();
        boton2.setText("Continuar");
        boton2.setFont(fuente3);
        boton2.setBounds(310, 330, 120, 30);
        boton2.addActionListener(this);
        
        // Titulos
        
        titulo1= new JLabel();
        titulo1.setText("PANOPTICO");
        titulo1.setFont(fuente);
        titulo1.setBounds(30, 120, 400, 65);
        
        titulo2= new JLabel();
        titulo2.setText("¡Bienvenido!");
        titulo2.setFont(fuente2);
        titulo2.setBounds(150, 230, 160, 30);
      
        // Ventana
        
        setTitle("PANOPTICO");
        setSize(460, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        setVisible(true);
        add(boton1);
        add(boton2);
        add(titulo1);
        add(titulo2);
        
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == boton1){
            this.dispose();
        }else if(ae.getSource() == boton2){
            //run();
            //this.dispose();
        }
    }

    @Override
    public void run() {
        try{
            Equipo chat = new Equipo("TMC MES.xlsx", 95, 90, 5, 95, 95, 25);
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "La base de datos del programa no existe o es inaccesible", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
