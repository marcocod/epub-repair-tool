/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epubrepairtool.gui;

import epubrepairtool.core.Epub;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Marco
 */
public class EpubTableModel extends DefaultTableModel{
    
    private final String[] HEADERS  = {"Local File Header Filename","Central Directory File Header Filename"};
    
    private JTable table;
    
    private String[][] filenames;
    
    private boolean rendering = false;
    
    public void setData(Epub epub, JTable table) {
        this.filenames=epub.getInvalidFilenames();
        this.table=table;
        this.rendering=true;        
        fireTableDataChanged();
    }
    
    public void unsetData(){
        this.filenames=null;
        this.table=null;
        this.rendering=false;
        fireTableDataChanged();
    }
    
    public int getRowCount() {
        if(rendering==false){
            return 0;
        }else{
            return filenames.length;
        }
    }

    public int getColumnCount() {
        return HEADERS.length;      
    }

    public String getColumnName(int col) {
        return HEADERS[col];
    }
    
    public Object getValueAt(int row, int col) {
      if (rendering==true) {
        if (col==0 || col==1) {
            return this.filenames[row][col];
        }else{
            return new String(" "); 
        }
      } else {
         return new String(" "); 
      }
    }
    
    public boolean isCellEditable(int row, int col){
        return false;
    }
    
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
}
