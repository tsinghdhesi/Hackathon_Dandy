
public class pair {
    private int row;
    private int col;
    
    public pair(int f, int s){
        row = f;
        col = s;
    }
    public int getRow(){
        return row;
    }
    public int getColumn(){
        return col;
    }
    public void setRow(int val){
        row = val;
    }
    public void setColumn(int val){
        col = val;
    }
    public boolean equals(pair x){
        if(x.getRow() == row && x.getColumn() == col){
            return true;
        }
        return false;
    }
}
