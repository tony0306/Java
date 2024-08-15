package bar;

import java.awt.*;				
import java.awt.event.*;	
import javax.swing.*;		

// MyJFrame(拉霸遊戲視窗)繼承JFrame視窗元件
// MyJFrame實作ActionListener介面的actionPerformed方法用來處理按鈕的按一下的事件
class MyJFrame extends JFrame implements ActionListener  {
	// 宣告jlbl[0]~jlbl[2]，宣告數組並初始化
    private JLabel[] jlbl = new JLabel[3];
    // 宣告 icon[0]~icon[3] 
    // 四個圖示依序為 0.jpg, 1.jpg, 2.jpg, 3.jpg
    private ImageIcon[] icons = new ImageIcon[4];
    // 宣告 jlblSum標籤用來顯示 "總數量：" 訊息
    // 宣告jlblBetting標籤用來顯示 "投注量：" 訊息
    private JLabel jlblSum, jlblBetting;
    // 宣告 jtxtBetting文字方塊用來讓使用者輸入投注量
    private JTextField jtxtBetting;
    // 宣告 jbtnOk "下注" 按鈕
    private JButton jbtnOk;
    private int sum = 100;
	//建構式
    MyJFrame()
    {
    	 // 不使用版面配置
    	 super.setLayout(null);
    	 // 視窗標題設為 "拉霸遊戲機"
    	 super.setTitle("拉霸遊戲機");
    	 
    	 // 設定icons[0]~icons[3]元件的圖示為barImg資料夾下的0.jpg~3.jpg
    	 for(int i=0; i<icons.length; i++){
    		 icons[i] = new ImageIcon(".\\barImg\\" + String.valueOf(i) + ".jpg");
    	 }
    	 
    	 // 建立jlbl[0]~jlbl[2]，並指定三個標籤為櫻桃圖(0.jpg)，最後在放入視窗內
    	 for (int i=0; i<jlbl.length; i++){
    		 jlbl[i] = new JLabel();
    		 jlbl[i].setBounds(i*100+10, 10, 86, 86);
    		 jlbl[i].setIcon(icons[0]);
    		 add(jlbl[i]);
    	 } 	 
    	 
    	 // 在視窗放入jlblSum標籤，該標籤顯示 "總數量："
    	 jlblSum = new JLabel("總數量：" + String.valueOf(sum));
    	 // 設定jlblSum標籤x座標10, y座標120, 寬160, 高20
    	 jlblSum.setBounds(10, 120, 160, 20);
    	 jlblSum.setFont(new Font("微軟中黑體",Font.PLAIN, 18));
    	 add(jlblSum);
    	 
    	// 在視窗放入jlblBetting標籤，該標籤顯示 "投注量："
    	 jlblBetting = new JLabel("投注量：");
    	 jlblBetting.setBounds(160, 120, 80, 20);
    	 jlblBetting.setFont(new Font("微軟中黑體",Font.PLAIN, 18));
    	 add(jlblBetting);
    	 
    	// 在視窗放入jtxtBetting文字方塊，讓使用輸入投注量
    	 jtxtBetting = new JTextField();
    	 jtxtBetting.setBounds(240, 120, 50, 25);
    	 jtxtBetting.setFont(new Font("微軟中黑體",Font.PLAIN, 18));
    	 add(jtxtBetting);
    	 
    	// 在視窗放入jbtnOk下注鈕 
    	 jbtnOk = new JButton("下注");
    	 jbtnOk.setBounds(10, 160, 80, 30);
    	 jbtnOk.setFont(new Font("微軟中黑體",Font.PLAIN, 18));
    	 add(jbtnOk);
    	 
    	 // 指定jbtnOk下注鈕的傾聽者為目前的物件
    	 // 因此按下注鈕時會執行目前類別的 actionPerformed 方法
    	 jbtnOk.addActionListener(this);
    	
    	 // 設定視窗大小為寬320, 高250
    	 setSize(320, 250);
    	 // 顯示視窗
    	 setVisible(true);
    	 // 設定按視窗的關閉鈕會結束程式
    	 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     }

     // 實作ActionListener介面的actionPerformed方法 
	 public void actionPerformed(ActionEvent evt) {   
		 // 建立執行緒t物件，並傳入Runnable介面物件
		 // 此執行緒用來啟動拉霸遊戲
		 // 讓jlbl[0]~jlbl[2]以亂數方式顯示櫻桃, 星星, 西瓜, bar四個圖示
		 // 並判斷是否中獎
		 Thread t = new Thread (
				 new Runnable(){
					 //實作Runnable介面的run方法
					 public void run(){
						 // k用來計算拉霸遊戲的換圖次數
						 // kind用來表示中獎倍數，kind等於-1表示沒中獎
						 int k=0, kind=-1;
						//n[0]~n[2] 用來存放產生的亂數值
						 int[] n = new int[jlbl.length]; 
					     int betting=0;		// 用來存放投注量
					     try{			    	
					    	 if(sum==0){
					    		 JOptionPane.showMessageDialog(null, "您已經破產了!即將離開遊戲");
					    		 System.exit(0);
					    	 }
					    	 // 取得使用者的投注額，並指定給betting
					    	 betting = Integer.parseInt(jtxtBetting.getText());
					    	 // 當總數量小於投注額或投注額小於0，表示金額不足並顯示在對話框裡
					    	 if (sum<betting || betting<=0) {
						         JOptionPane.showMessageDialog(null, "金額不足或金額不對!");
						         return ;
					    	 }
					    	 sum-=betting;		
					         jlblSum.setText("總數量：" + String.valueOf(sum));
					         // 按下注鈕啟動拉霸遊戲機後馬上即停用下注鈕
					         // 防止使用者重複按下
							 jbtnOk.setEnabled(false);		
					     }catch(Exception ex){
					         JOptionPane.showMessageDialog(null, "請輸入數字");
					    	 return ;	    	
					     }
					     
						 try{
							 do{
								 // 產生0~3之間的亂數並指定給n[0]~n[2]
								 // 並在jlbl[0]~jlbl[2]隨機顯示櫻桃, 星星, 西瓜, bar圖示
								 for (int i=0; i<jlbl.length; i++){
									 n[i]=(int)Math.round(Math.random()*3);
									 jlbl[i].setIcon(icons[n[i]]);
								 }
								 k++;
								//目前執行緒暫停0.1秒
								 Thread.currentThread().sleep(100);			
							 }while(k<10);	// 若k大於0，則停止拉霸遊戲
						 }catch(InterruptedException ex){ }
						 
						 // 判斷中獎倍率
					     if (n[0]==0 && n[1]==0 && n[2]==0){			
					    	 kind = 3;	//三個圖為櫻桃, 得3倍
					     }else if(n[0]==1 && n[1]==1 && n[2]==1){	
					    	 kind = 10;	//三個圖為星星, 得10倍
					     }else if(n[0]==2 && n[1]==2 && n[2]==2){	
					    	 kind = 20;	//三個圖為西瓜, 得20倍
					     }else if(n[0]==3 && n[1]==3 && n[2]==3){	
					    	 kind = 50;	//三個圖為bar, 得50倍
					     }
					     
					   	 // 判斷是否中獎，若kind不等於-1表示中獎
					     if (kind!=-1){
					         JOptionPane.showMessageDialog(null, "中獎得" + String.valueOf(kind) + "倍");
					         // 目前總數量(總投注額)累加中獎數量
					         sum += kind*betting;		
					         jlblSum.setText("總數量：" + String.valueOf(sum));
					     }
					     // 重新下注鈕啟用
						 jbtnOk.setEnabled(true);		
	            }
	      });
	      t.start();		// 啟動執行緒
	  } 
}
// 主程式
public class Program {
	public static void main(String[] args){
		// 建立MyJFrame視窗(拉霸遊戲)
		new MyJFrame();
	}
}
