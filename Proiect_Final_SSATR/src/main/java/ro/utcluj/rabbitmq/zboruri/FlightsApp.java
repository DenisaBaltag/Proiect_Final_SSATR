/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ro.utcluj.rabbitmq.zboruri;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author denisa
 */
public class FlightsApp extends javax.swing.JFrame implements DeliverCallback, Runnable{
    
    private String FLIGHT_QUEUE_NAME="flight-";
    private ArrayList<String> activeOrders = new ArrayList<>();
    private FlightsApp.ListOrderesModel listModel = new ListOrderesModel();
    private String flightId;
    private String currentOrder = null;
     /**
     * Creates new form FlightApp
     */
    public FlightsApp() {
        initComponents();
        new Thread(this).start();
    }
    
     public FlightsApp(String flightId) {
        initComponents();
        this.flightId = flightId;
        FLIGHT_QUEUE_NAME = "flight-"+flightId;
        this.setTitle("Flight App: "+flightId);
        new Thread(this).start();
    }
   
   
    public void run(){
        try {
            startReceiver();
        } catch (IOException ex) {
            Logger.getLogger(FlightsApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(FlightsApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Start consuming messages received on flight queue. This is a blocking method and must be called from inside a separate thread similar as client.
     * @throws IOException
     * @throws TimeoutException 
     */
    public void startReceiver() throws IOException, TimeoutException{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(FLIGHT_QUEUE_NAME, false, false, false, null);      
        channel.queueBind(FLIGHT_QUEUE_NAME, "booking-requests", "");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        System.out.println("Start consuming messages...");
        channel.basicConsume(FLIGHT_QUEUE_NAME, true, this, consumerTag -> { });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listOrders = new javax.swing.JList<>();
        bTake = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tfCurrentOrder = new javax.swing.JTextField();
        bComplete = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        listOrders.setModel(listModel);
        jScrollPane1.setViewportView(listOrders);

        bTake.setText("Rezerva");
        bTake.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTakeActionPerformed(evt);
            }
        });

        jLabel1.setText("Atribuie zbor:");

        tfCurrentOrder.setEditable(false);

        bComplete.setText("Termina ");
        bComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCompleteActionPerformed(evt);
            }
        });

        jLabel2.setText("Utilizatori care cauta zboruri");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bComplete, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                            .addComponent(tfCurrentOrder))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bTake, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bTake)
                .addGap(48, 48, 48))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfCurrentOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(bComplete)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Send client confirmation that order has been accepted. 
     * @param destination
     * @param msg
     * @throws Exception 
     */
    public void sendOrderConfirmationToClient(String destination, String msg) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); 
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.queueDeclare(destination, false, false, false, null);
            channel.basicPublish("", destination, null, msg.getBytes());            
            System.out.println(" [x] Sent '" + msg + "'");
        }
    }
    
    public void sendOrderConfirmationToOtherFlights(String destination, String msg) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.basicPublish(destination, "", null, msg.getBytes());
            System.out.println(" [x] Sent '" + msg + "'");
        }
    }
    
    
    private void bTakeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTakeActionPerformed
        try {
           
            currentOrder = "orderconfirmation_"+this.listOrders.getSelectedValue();
            sendOrderConfirmationToClient(currentOrder, flightId);            
            sendOrderConfirmationToOtherFlights("booking-requests", "TAKEN "+listOrders.getSelectedValue());
            this.tfCurrentOrder.setText(this.listOrders.getSelectedValue());
            listOrders.updateUI();
            this.bTake.setEnabled(false);
        } catch (Exception ex) {
            Logger.getLogger(FlightsApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bTakeActionPerformed

    private void bCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCompleteActionPerformed
        this.bTake.setEnabled(true);
        try {
            sendOrderConfirmationToClient(currentOrder, flightId);
            currentOrder = null;
            this.tfCurrentOrder.setText("");
        } catch (Exception ex) {
            Logger.getLogger(FlightsApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_bCompleteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FlightsApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FlightsApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FlightsApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FlightsApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FlightsApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bComplete;
    private javax.swing.JButton bTake;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> listOrders;
    private javax.swing.JTextField tfCurrentOrder;
    // End of variables declaration//GEN-END:variables

  
    /**
     * Call when a message is received on flight queue.
     * @param string
     * @param dlvr
     * @throws IOException 
     */
       @Override
        public void handle(String string, Delivery dlvr) throws IOException {
            String message = new String(dlvr.getBody(), "UTF-8");
            
            if(message.startsWith("TAKEN")){                
                message = message.split("\\s+")[1];
                activeOrders.remove(message);
            }
            else if(!activeOrders.contains(message)){                
                activeOrders.add(message);
            }
                      
            this.listOrders.updateUI();
        }
        
    /**
     * This class is used to client a list model required to display list of order inside JList UI component. 
     * 
     */
    class ListOrderesModel implements ListModel{
  
        @Override
        public int getSize() {
            return activeOrders.size();
        }

        @Override
        public Object getElementAt(int index) {
            return activeOrders.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {            
        }

        @Override
        public void removeListDataListener(ListDataListener l) {          
        }
    }
}
