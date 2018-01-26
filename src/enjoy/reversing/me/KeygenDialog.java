package enjoy.reversing.me;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class KeygenDialog
{
  private JPanel rootPanel;
  private JTextField nameTextField;
  private JTextArea licenseArea;
  private JTextArea requestArea;
  private JTextArea responseArea;
  private JTextField loaderTextField;
  private JButton runButton;
  
  private KeygenDialog()
  {
    $$$setupUI$$$();nameTextField.getDocument().addDocumentListener(new DocumentListener()
    {
      public void insertUpdate(DocumentEvent e)
      {
        if (nameTextField.getText().length() > 0) {
          licenseArea.setText(KeygenDialog.this.generateLicense(nameTextField.getText()));
        }
      }
      
      public void removeUpdate(DocumentEvent e)
      {
        if (nameTextField.getText().length() > 0) {
          licenseArea.setText(KeygenDialog.this.generateLicense(nameTextField.getText()));
        }
      }
      
      public void changedUpdate(DocumentEvent e)
      {
        if (nameTextField.getText().length() > 0) {
          licenseArea.setText(KeygenDialog.this.generateLicense(nameTextField.getText()));
        }
      }
    }
    
      );requestArea.getDocument().addDocumentListener(new DocumentListener()
    {
      public void insertUpdate(DocumentEvent e)
      {
        if (requestArea.getText().length() > 0) {
          responseArea.setText(KeygenDialog.this.generateActivation(requestArea.getText()));
        }
      }
      
      public void removeUpdate(DocumentEvent e)
      {
        if (requestArea.getText().length() > 0) {
          responseArea.setText(KeygenDialog.this.generateActivation(requestArea.getText()));
        }
      }
      
      public void changedUpdate(DocumentEvent e)
      {
        if (requestArea.getText().length() > 0) {
          responseArea.setText(KeygenDialog.this.generateActivation(requestArea.getText()));
        }
      }
    });loaderTextField.addPropertyChangeListener(new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent evt)
      {
        String filename = new File(KeygenDialog.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
        
        File f = null;
        String current_dir = null;
        try
        {
          f = new File(KeygenDialog.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
          if (f.isDirectory()) {
            current_dir = f.getPath();
          } else {
            current_dir = f.getParentFile().toString();
          }
          System.out.print(current_dir);
        }
        catch (URISyntaxException e)
        {
          e.printStackTrace();
        }
        long newest_time = 0L;
        String newest_file = "burpsuite_jar_not_found.jar";
        try
        {
          DirectoryStream<Path> dirStream = Files.newDirectoryStream(
            Paths.get(current_dir, new String[0]), "burpsuite_*.jar");Throwable localThrowable3 = null;
          try
          {
            for (Path path : dirStream)
            {
              System.out.print(path);
              if (!Files.isDirectory(path, new LinkOption[0]))
              {
                System.out.print(path);
                if (newest_time < path.toFile().lastModified())
                {
                  newest_time = path.toFile().lastModified();
                  newest_file = path.getFileName().toString();
                }
              }
            }
          }
          catch (Throwable localThrowable5)
          {
            localThrowable3 = localThrowable5;throw localThrowable5;
          }
          finally
          {
            if (dirStream != null) {
              if (localThrowable3 != null) {
                try
                {
                  dirStream.close();
                }
                catch (Throwable localThrowable2)
                {
                  localThrowable3.addSuppressed(localThrowable2);
                }
              } else {
                dirStream.close();
              }
            }
          }
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        if (newest_time != 0L) {
          runButton.setEnabled(true);
        }
        loaderTextField.setText("java -Xbootclasspath/p:" + filename + " -jar " + newest_file);
      }
    });runButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        try
        {
          Runtime.getRuntime().exec(loaderTextField.getText());
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
      }
    });nameTextField.addPropertyChangeListener(new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent evt)
      {
        licenseArea.setText(KeygenDialog.this.generateLicense(nameTextField.getText()));
      }
    });
  }
  
  private ArrayList<String> decodeActivationRequest(String activationRequest)
  {
    try
    {
      byte[] rawBytes = decrypt(Base64.getDecoder().decode(activationRequest));
      
      ArrayList<String> ar = new ArrayList();
      
      int from = 0;
      for (int i = 0; i < rawBytes.length; i++) {
        if (rawBytes[i] == 0)
        {
          ar.add(new String(rawBytes, from, i - from));
          
          from = i + 1;
        }
      }
      ar.add(new String(rawBytes, from, rawBytes.length - from));
      if (ar.size() != 5)
      {
        System.out.print("Activation Request Decoded to wrong size! The following was Decoded: \n");
        System.out.print(ar);
        return null;
      }
      return ar;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return null;
  }
  
  private String generateActivation(String activationRequest)
  {
    ArrayList<String> request = decodeActivationRequest(activationRequest);
    if (request == null) {
      return "Error decoding activation request :-(";
    }
    String[] responseArray = { "0.4315672535134567", (String)request.get(0), "activation", (String)request.get(1), "True", "", (String)request.get(2), (String)request.get(3), "xMoYxfewJJ3jw/Zrqghq1nMHJIsZEtZLu9kp4PZw+kGt+wiTtoUjUfHyTt/luR3BjzVUj2Rt2tTxV2rjWcuV7MlwsbFrLOqTVGqstIYA1psSP/uspFkkhFwhMi0CJNRHdxe+xPYnXObzi/x6G4e0wH3iZ5bnYPRfn7IHiV1TVzslQur/KR5J8BG8CN3B9XaS8+HJ90Hn4sy81fW0NYRlNW8m5k4rMDNwCLvDzp11EN//wxYEdruNKqtxEvv6VesiFOg711Y6g/9Nf91C5dFedNEhPv2k2fYb4rJ+z1mCOBSmWIzjGlS1r2xOzITrrrMkr+ilBE3VFPPbES4KsRh/fw==", "tdq99QBI3DtnQQ7rRJLR0uAdOXT69SUfAB/8O2zi0lsk4/bXkM58TP6cuhOzeYyrVUJrM11IsJhWrv8SiomzJ/rqledlx+P1G5B3MxFVfjML9xQz0ocZi3N+7dHMjf9/jPuFO7KmGfwjWdU4ItXSHFneqGBccCDHEy4bhXKuQrA=" };
    
    return prepareArray(responseArray);
  }
  
  private String generateLicense(String licenseName)
  {
    // 授权到期时间 + xx 年
    String[] licenseArray = { getRandomString(), "license", licenseName, String.valueOf(new Date().getTime() + 5000000000000L), "1", "full", "tdq99QBI3DtnQQ7rRJLR0uAdOXT69SUfAB/8O2zi0lsk4/bXkM58TP6cuhOzeYyrVUJrM11IsJhWrv8SiomzJ/rqledlx+P1G5B3MxFVfjML9xQz0ocZi3N+7dHMjf9/jPuFO7KmGfwjWdU4ItXSHFneqGBccCDHEy4bhXKuQrA=" };
    
    return prepareArray(licenseArray);
  }
  
  private String prepareArray(String[] array)
  {
    try
    {
      ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
      for (int i = 0; i < array.length - 1; i++)
      {
        byteArray.write(array[i].getBytes());
        byteArray.write(0);
      }
      byteArray.write(array[(array.length - 1)].getBytes());
      
      return new String(Base64.getEncoder().encode(encrypt(byteArray.toByteArray())));
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
  }
  
  private static byte[] encryption_key = "burpr0x!".getBytes();
  
  private byte[] encrypt(byte[] arrayOfByte)
  {
    try
    {
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(encryption_key, "DES");
      Cipher localCipher = Cipher.getInstance("DES");
      localCipher.init(1, localSecretKeySpec);
      return localCipher.doFinal(arrayOfByte);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
  }
  
  private byte[] decrypt(byte[] arrayOfByte)
  {
    try
    {
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(encryption_key, "DES");
      Cipher localCipher = Cipher.getInstance("DES");
      localCipher.init(2, localSecretKeySpec);
      return localCipher.doFinal(arrayOfByte);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
  }
  
  private String getRandomString()
  {
    String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuilder str = new StringBuilder();
    Random rnd = new Random();
    while (str.length() < 32)
    {
      int index = (int)(rnd.nextFloat() * CHARS.length());
      str.append(CHARS.charAt(index));
    }
    return str.toString();
  }
  
  public static void main(String[] args)
    throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException
  {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    JFrame frame = new JFrame("BurpSuite Loader & Keygen - By surferxyz");
    frame.setContentPane(new KeygenDialog().rootPanel);
    frame.setDefaultCloseOperation(3);
    frame.pack();
    int       screenWidth  = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
    int       screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
    Dimension d            = frame.getPreferredSize();
    frame.setBounds(screenWidth / 2 - (d.width / 2), screenHeight / 2 - (d.height / 2), d.width, d.height);
    frame.setSize(870, 500);
    
    frame.setVisible(true);
  }
  
  private void $$$setupUI$$$()
  {
    rootPanel = new JPanel();
    rootPanel.setLayout(new GridLayoutManager(9, 2, new Insets(3, 3, 3, 3), -1, -1));
//    rootPanel.setPreferredSize(new Dimension(800, 480));
    rootPanel.setBorder(BorderFactory.createTitledBorder(null, "BurpSuite Loader & Keygen - By surferxyz", 2, 2));
    JLabel label1 = new JLabel();
    label1.setText("License文本：");
    rootPanel.add(label1, new GridConstraints(4, 0, 1, 1, 4, 0, 0, 0, null, null, null, 0, false));
    nameTextField = new JTextField();
    nameTextField.setHorizontalAlignment(0);
    nameTextField.setText("Licensed To surferxyz");
    rootPanel.add(nameTextField, new GridConstraints(4, 1, 1, 1, 0, 1, 0, 0, null, new Dimension(150, -1), null, 0, false));
    JLabel label2 = new JLabel();
    label2.setText("License: ");
    rootPanel.add(label2, new GridConstraints(5, 0, 1, 1, 4, 0, 0, 0, null, null, null, 0, false));
    JLabel label3 = new JLabel();
    label3.setText("Activation Request: ");
    rootPanel.add(label3, new GridConstraints(6, 0, 1, 1, 4, 0, 0, 0, null, null, null, 0, false));
    JLabel label4 = new JLabel();
    label4.setText("Activation Response:");
    rootPanel.add(label4, new GridConstraints(7, 0, 1, 1, 4, 0, 0, 0, null, null, null, 0, false));
    Spacer spacer1 = new Spacer();
    rootPanel.add(spacer1, new GridConstraints(8, 0, 1, 1, 0, 2, 1, 4, null, null, null, 0, false));
    Spacer spacer2 = new Spacer();
    rootPanel.add(spacer2, new GridConstraints(8, 1, 1, 1, 0, 2, 1, 4, null, null, null, 0, false));
    JScrollPane scrollPane1 = new JScrollPane();
    rootPanel.add(scrollPane1, new GridConstraints(5, 1, 1, 1, 0, 3, 5, 0, null, null, null, 0, false));
    licenseArea = new JTextArea();
    licenseArea.setEditable(false);
    Font licenseAreaFont = UIManager.getFont("TextField.font");
    if (licenseAreaFont != null) {
      licenseArea.setFont(licenseAreaFont);
    }
    licenseArea.setLineWrap(true);
    licenseArea.setRows(5);
    scrollPane1.setViewportView(licenseArea);
    JScrollPane scrollPane2 = new JScrollPane();
    rootPanel.add(scrollPane2, new GridConstraints(6, 1, 1, 1, 0, 3, 5, 0, null, null, null, 0, false));
    requestArea = new JTextArea();
    Font requestAreaFont = UIManager.getFont("TextField.font");
    if (requestAreaFont != null) {
      requestArea.setFont(requestAreaFont);
    }
    requestArea.setLineWrap(true);
    requestArea.setRows(5);
    scrollPane2.setViewportView(requestArea);
    JScrollPane scrollPane3 = new JScrollPane();
    rootPanel.add(scrollPane3, new GridConstraints(7, 1, 1, 1, 0, 3, 5, 0, null, null, null, 0, false));
    responseArea = new JTextArea();
    Font responseAreaFont = UIManager.getFont("TextField.font");
    if (responseAreaFont != null) {
      responseArea.setFont(responseAreaFont);
    }
    responseArea.setLineWrap(true);
    responseArea.setRows(8);
    scrollPane3.setViewportView(responseArea);
    JLabel label5 = new JLabel();
    label5.setRequestFocusEnabled(false);
    label5.setText("启动命令：");
    rootPanel.add(label5, new GridConstraints(3, 0, 1, 1, 4, 0, 0, 0, null, null, null, 0, false));
    JLabel label6 = new JLabel();
    label6.setText("1. 使用此载入器来执行burpsuite，命令位于下面的文本框中，也可以直接点击运行来执行。");
    rootPanel.add(label6, new GridConstraints(0, 1, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
    JLabel label7 = new JLabel();
    label7.setText("2. 使用Manual activation方式注册。");
    rootPanel.add(label7, new GridConstraints(1, 1, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
    JLabel label8 = new JLabel();
    label8.setText("3. 注册成功后您需要使用本加载器或同目录的另一个jar来启动BurpSuite，否则会变成未注册状态。");
    rootPanel.add(label8, new GridConstraints(2, 1, 1, 1, 8, 0, 0, 0, null, null, null, 0, false));
    JLabel label9 = new JLabel();
    label9.setText("操作说明：");
    rootPanel.add(label9, new GridConstraints(1, 0, 1, 1, 4, 0, 0, 0, null, null, null, 0, false));
    JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    rootPanel.add(panel1, new GridConstraints(3, 1, 1, 1, 0, 3, 3, 3, null, null, null, 0, false));
    loaderTextField = new JTextField();
    loaderTextField.setEditable(false);
    loaderTextField.setHorizontalAlignment(0);
    loaderTextField.setText("");
    panel1.add(loaderTextField, new GridConstraints(0, 0, 1, 1, 0, 1, 4, 0, null, new Dimension(150, -1), null, 0, false));
    runButton = new JButton();
    runButton.setEnabled(false);
    runButton.setText("执行");
    runButton.setVerticalAlignment(1);
    panel1.add(runButton, new GridConstraints(0, 1, 1, 1, 4, 0, 0, 0, null, null, null, 0, false));
    label1.setLabelFor(nameTextField);
    label2.setLabelFor(licenseArea);
    label3.setLabelFor(requestArea);
    label4.setLabelFor(responseArea);
  }
  
  public JComponent $$$getRootComponent$$$()
  {
    return rootPanel;
  }
}
