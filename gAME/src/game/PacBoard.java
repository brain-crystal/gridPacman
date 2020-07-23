/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
/**
 *
 * @author Shweta
 */
public class PacBoard extends JPanel implements ActionListener{
    private int numOfBlocks=28;
    private int totBlocks=numOfBlocks*numOfBlocks;
    private int blockSize=24;
    int[][] obstacles=new int[numOfBlocks][numOfBlocks];
    int[][] dots=new int[numOfBlocks][numOfBlocks];
    int[][] shpath=new int[totBlocks][totBlocks];
    public int numOfDots,numOfObstacles;
    boolean left,right,up,down;
    private int scrsize=numOfBlocks*24;
    private int pacx,pacy;
    private Image pacman,p2left,p2right,p2up,p2down;
    private Image p3left,p3right,p3up,p3down;
    private Image p4left,p4right,p4up,p4down;
    private Image ghost;
    private Image pacman_game;
    private int currAnim=0,dir=1;
    private int delay=0;
    private int dx=0,dy=0;
    private int timeElapsed=0;
    public boolean dying=false;
    private int BOARD_WIDTH=numOfBlocks*24,BOARD_HEIGHT=numOfBlocks*24;
    private Timer timer;
    int curr_i,curr_j;
    String gongFile = "pacman_beginning.wav";
    int numOfGhosts=4;
    Point ghosts[]=new Point[numOfGhosts];
    int gdx[]=new int[numOfGhosts];
    int gdy[]=new int[numOfGhosts];
    Audio a;
    int noOfdots=0;
    int initialDelay=0;
    private int HighScore;
    private Image[] ghost_images=new Image[numOfGhosts];
    public PacBoard()
    {
        setSize(numOfBlocks*24,numOfBlocks*24);
        left=false;
        right=true;
        up=false;
        down=false;
        loadImages();
        initBoard();
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setVisible(true);
        new Thread(
            new Runnable() {
                @Override
                public void run() {
                    try {
                        // PLAY AUDIO CODE
                        
                       
                        //InputStream in = new FileInputStream(gongFile);
                        // create an audiostream from the inputstream
                        //AudioStream audioStream = new AudioStream(in);
                        // play the audio clip with the audioplayer class
                        //AudioPlayer.player.start(audioStream);
                        a=new Audio(gongFile,true);
                        a.musicStart();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        timer = new Timer(150, this);
        timer.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
    void loadImages()
    {
        try{
            pacman=ImageIO.read(getClass().getResource("pacman.png"));
            p2left=ImageIO.read(getClass().getResource("left1.png"));
            p3left=ImageIO.read(getClass().getResource("left2.png"));
            p4left=ImageIO.read(getClass().getResource("left3.png"));
            p2right=ImageIO.read(getClass().getResource("right1.png"));
            p3right=ImageIO.read(getClass().getResource("right2.png"));
            p4right=ImageIO.read(getClass().getResource("right3.png"));
            p2up=ImageIO.read(getClass().getResource("up1.png"));
            p3up=ImageIO.read(getClass().getResource("up2.png"));
            p4up=ImageIO.read(getClass().getResource("up3.png"));
            p2down=ImageIO.read(getClass().getResource("down1.png"));
            p3down=ImageIO.read(getClass().getResource("down2.png"));
            p4down=ImageIO.read(getClass().getResource("down3.png"));
            ghost_images[0]=ImageIO.read(getClass().getResource("pghost3.png"));
            ghost_images[1]=ImageIO.read(getClass().getResource("ghost2.jpg"));
            ghost_images[2]=ImageIO.read(getClass().getResource("ghost3.jpg"));
            ghost_images[3]=ImageIO.read(getClass().getResource("ghost4.jpg"));
            pacman_game=ImageIO.read(getClass().getResource("pacman-game.jpg"));
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    void initBoard()
    {
        for(int i=0;i<numOfBlocks;i++)
            for(int j=0;j<numOfBlocks;j++)
                obstacles[i][j]=0;
        numOfObstacles=100;
        putObstacles();
        putDots();
        //initGhosts();
        calShortestPath();
        initPacman();
        initGhosts();
    }
    void calShortestPath()
    {
        for(int k=0;k<totBlocks;k++)
        {
            for(int j=0;j<totBlocks;j++)
            {
                shpath[k][j]=Integer.MAX_VALUE;
            }
        }
        for(int k=0;k<totBlocks;k++)
        {
           Queue<Pair> q=new LinkedList<Pair>();
           q.offer(new Pair(k,0));
           HashMap hm=new HashMap();
           hm.put(k, 1);
           while(!q.isEmpty())
           {
               Pair p=q.poll();
               int i,j;
               shpath[k][p.vertex]=p.distance;
               i=p.vertex/numOfBlocks;
               j=p.vertex%numOfBlocks;
               if(i>0&&obstacles[i-1][j]==0)
               {
                    if(!hm.containsKey((i-1)*numOfBlocks+j))
                    {
                        hm.put((i-1)*numOfBlocks+j, 1);
                        q.offer(new Pair((i-1)*numOfBlocks+j,p.distance+1));
                    }
               }
               else if(i==0&&obstacles[numOfBlocks-1][j]==0)
               {
                    if(!hm.containsKey((numOfBlocks-1)*numOfBlocks+j))
                    {
                        hm.put((numOfBlocks-1)*numOfBlocks+j, 1);
                        q.offer(new Pair((numOfBlocks-1)*numOfBlocks+j,p.distance+1));
                    }
               }
               if(j>0&&obstacles[i][j-1]==0)
               {
                    if(!hm.containsKey((i)*numOfBlocks+j-1))
                    {
                        hm.put(i*numOfBlocks+j-1, 1);
                        q.offer(new Pair(i*numOfBlocks+j-1,p.distance+1));
                    }
               }
               else if(j==0&&obstacles[i][numOfBlocks-1]==0)
               {
                   if(!hm.containsKey(i*numOfBlocks+numOfBlocks-1))
                   {
                        hm.put(i*numOfBlocks+numOfBlocks-1, 1);
                        q.offer(new Pair(i*numOfBlocks+numOfBlocks-1,p.distance+1));
                   }
               }
               if(i<numOfBlocks-1&&obstacles[i+1][j]==0)
               {
                   if(!hm.containsKey((i+1)*numOfBlocks+j))
                   {
                       hm.put((i+1)*numOfBlocks+j, 1);
                       q.offer(new Pair((i+1)*numOfBlocks+j,p.distance+1));
                   }
               }
               else if(i==numOfBlocks-1&&obstacles[0][j]==0)
               {
                   if(!hm.containsKey(j))
                   {
                       hm.put(j,1);
                       q.offer(new Pair(j, p.distance+1));
                   }
               }
               if(j<numOfBlocks-1&&obstacles[i][j+1]==0)
               {
                   if(!hm.containsKey(i*numOfBlocks+j+1))
                   {
                       hm.put(i*numOfBlocks+j+1, 1);
                       q.offer(new Pair(i*numOfBlocks+j+1,p.distance+1));
                   }
               }
               else if(j==numOfBlocks-1&&obstacles[i][0]==0)
               {
                   if(!hm.containsKey(i*numOfBlocks))
                   {
                       hm.put(i*numOfBlocks,1);
                       q.offer(new Pair(i*numOfBlocks, p.distance+1));
                   }
               }
           }
        }
       /* System.out.println("The shortest path matrix is");
        for(int k=0;k<totBlocks;k++)
        {
            for(int j=0;j<totBlocks;j++)
            {
                System.out.print(shpath[k][j]+" ");
            }
            System.out.println("");
        }*/
    }
    void putObstacles()
    {
        int c=0,i,j;
        System.out.println("Obstacles are:");
        while(c<numOfObstacles)
        {
            i=(int)(Math.random()*numOfBlocks);
            j=(int)(Math.random()*numOfBlocks);
            if(obstacles[i][j]==0)
            {
                if(checkIfSuitable(i,j))
                {
                    obstacles[i][j]=1;
                    c++;
                }
            }
        }     
    }
    void putDots()
    {
       // System.out.println("Dots and obstacles are:");
        numOfDots=0;
        for(int i=0;i<numOfBlocks;i++)
        {
            for(int j=0;j<numOfBlocks;j++)
            {
                if(obstacles[i][j]==0)
                {
                    if(checkIfBounded(i,j))
                    {
                      //  g2.fillRect(i*24+8,j*24+8,2,2);
                        dots[i][j]=1;
                        numOfDots++;
                    }
                }
              //  System.out.print(dots[i][j]+":"+obstacles[i][j]+" ");
            }
           // System.out.println("");
        }
        noOfdots=0;
    }
    boolean checkIfSuitable(int i,int j)
    {
        if((i>0&&obstacles[i-1][j]==0)||i==0)
        {
            if((i<numOfBlocks-1&&obstacles[i+1][j]==0)||i==numOfBlocks-1)
            {
                if((j>0&&obstacles[i][j-1]==0)||j==0)
                {
                    if((j<numOfBlocks-1&&obstacles[i][j+1]==0)||j==numOfBlocks-1)
                        return true;
                }
            }
        }
        return false;
    }
    void drawMaze(Graphics2D g)
    {
        //Graphics2D g2=(Graphics2D)g.create();
        int r,gr,b;
        r=(int)(Math.random()*256);
        gr=(int)(Math.random()*256);
        b=(int)(Math.random()*256);
        GradientPaint gp1= new GradientPaint(5,20,new Color(r,gr,b),20,25,Color.black,true);
        g.setPaint(gp1);
        //g2.fillRect(0,0,getParent().getWidth(),getParent().getHeight());
        for(int i=0;i<numOfBlocks;i++)
        {
            for(int j=0;j<numOfBlocks;j++)
            {
                if(obstacles[i][j]==1)
                {
                    g.fillRect(j*24,i*24,24,24);
                }
            }
        }
        g.setColor(Color.WHITE);
        for(int i=0;i<numOfBlocks;i++)
        {
            for(int j=0;j<numOfBlocks;j++)
            {
                if(dots[i][j]==1)
                {
                    g.fillRect(j*24+8,i*24+8,2,2);
                }
            }
        }
        
    }
    boolean checkIfBounded(int i,int j)
    {
        if((i>0&&obstacles[i-1][j]==0)||(i==0&&obstacles[numOfBlocks-1][j]==0)||(j>0&&obstacles[i][j-1]==0)||(j==0&&obstacles[numOfBlocks-1][j]==0)||(i<numOfBlocks-1&&obstacles[i+1][j]==0)||(i==numOfBlocks-1&&obstacles[0][j]==0)||(j<numOfBlocks-1&&obstacles[i][j+1]==0)||(j==numOfBlocks-1&&obstacles[i][0]==0))
            return true;
        return false;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(initialDelay<20)
        {
            drawEverything(g);
            initialDelay++;
        }
        else
        {
            gameUpdate(g);
            //if(!timer.isRunning())
            //{
              //  drawGameOver((Graphics2D)g.create());
            //}
        }
    }
    void drawEverything(Graphics g)
    {
        Graphics2D g2=(Graphics2D)g.create();
        drawMaze(g2);
        drawGhosts(g2);
        drawPacman(g2);
    }
    int endDelay=0;
    void gameUpdate(Graphics g)
    {
        Graphics2D g2=(Graphics2D)g.create();
        if (dying) {
              endDelay++;
          //  death();
            if(endDelay>=20){
                drawGameOver(g2); 
                
                timer.stop();
                 //JOptionPane.showMessageDialog(null, "Your Score is "+numOfDots*100,"Game Over!!",1);
                 
            }
            else
            {
                 drawMaze(g2);
                 drawGhosts(g2);
                 drawPacman(g2);
            }
                    

        } else {

            drawMaze(g2);
            changeAnim();
            moveGhosts();
            drawGhosts(g2);
            movePacman();
            drawPacman(g2);
            checkGameOver();
        }
    }
    void drawGameOver(Graphics2D g)
    {
        GridPacman.gamePlaying=false;
        GridPacman.gameStatus=2;
        System.out.println("game finished");
        GridPacman.score=noOfdots*10;
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 30);
        FontMetrics fm = getFontMetrics(small);
        GradientPaint gp1= new GradientPaint(0,0,Color.GREEN,BOARD_WIDTH,BOARD_HEIGHT,Color.black);
        g.setPaint(gp1);
        g.fillRect(0,0,BOARD_WIDTH,BOARD_HEIGHT);
        g.setColor(Color.BLACK);
        g.setFont(small);
        g.drawString(msg, (BOARD_WIDTH - fm.stringWidth(msg)) / 2,
                BOARD_HEIGHT / 2-40);
        if(noOfdots==numOfDots)
        {
            g.setColor(Color.BLACK);
            g.setFont(small);
            g.drawString("You Won!!",(BOARD_WIDTH - fm.stringWidth(msg)) / 2-10,BOARD_HEIGHT/2+30);
         
            a.musicStop();
        }
        else
        {
            g.setColor(Color.BLACK);
            g.setFont(small);
            g.drawString("You Lose!!", (BOARD_WIDTH - fm.stringWidth(msg)) / 2,BOARD_HEIGHT/2+30);
            g.drawString("Score : "+noOfdots*10,(BOARD_WIDTH - fm.stringWidth(msg)) / 2-10,BOARD_HEIGHT/2+100);
            
        }
        //JOptionPane.showMessageDialog(this, "Game Over!! Your Score is "+numOfDots);
    }
    void checkGameOver()
    {
        for(int i=0;i<numOfGhosts;i++){
            if(ghosts[i].x==curr_i&&ghosts[i].y==curr_j)
            {
               dying=true;
            }
        }
    }
    void changeAnim()
    {
        if(timeElapsed==delay)
        {
            currAnim=currAnim+dir;
            timeElapsed=0;
            if(currAnim==3||currAnim==0)
            {
                dir=-dir;
            }
        }
        else
            timeElapsed+=1;
            
    }
    void initPacman()
    {
        int i;
        for(i=0;i<numOfBlocks;i++)
        {
            if(obstacles[i][0]==0)
                break;
        }
        pacy=(i*24);
        pacx=0;
        //System.out.println(i+" "+pacx+" "+pacy);
        dx=dy=0;
        curr_i=i;
        curr_j=0;
        right=true;
        left=up=down=false;
    }
    void initGhosts()
    {
        int i,j,c=0;
        /*for(i=numOfBlocks+5;i<numOfBlocks;i++)
        {
            for(j=numOfBlocks+5;j<numOfBlocks;j++)
            {
                if(obstacles[i][j]==0&&(!(curr_i==i&&curr_j==j))&&checkIfBounded(i, j))
                {
                   ghosts[c]=new Point(i,j); 
                   System.out.println("ghost at"+ghosts[c]);
                   c++;
                   System.out.println("c="+c);
                   if(c==numOfGhosts-2)
                       break;
                }
            }
            if(c==numOfGhosts-2)
                break;
        }
        for(i=numOfBlocks+5;i<numOfBlocks;i++)
        {
            for(j=numOfBlocks+5;j<numOfBlocks;j++)
            {
                if(obstacles[i][j]==0&&(!(curr_i==i&&curr_j==j))&&checkIfBounded(i, j))
                {
                   ghosts[c]=new Point(i,j); 
                   System.out.println("ghost at"+ghosts[c]);
                   c++;
                   System.out.println("c="+c);
                   if(c==numOfGhosts-2)
                       break;
                }
            }
            if(c==numOfGhosts-2)
                break;
        }*/
        while(c<numOfGhosts)
        {
            i=(int)(Math.random()*numOfBlocks);
            j=(int)(Math.random()*numOfBlocks);
            /*if(Math.abs(curr_i-i)<=5||Math.abs(curr_j-j)<=5)
            {
                i=i+6;
                j=j+5;
                
            }*/
            //i=ThreadLocalRandom.current().nextInt(6, 24 + 1);
            //j=ThreadLocalRandom.current().nextInt(6, 24 + 1);
            if(obstacles[i][j]==0&&(!(curr_i==i&&curr_j==j))&&checkIfBounded(i, j))
            {
                int k=0;
                for(k=0;k<c;k++)
                {
                    if(ghosts[k].x==i&&ghosts[k].y==j)
                    {
                        break;
                    }
                }
                if(k==c)
                {
                    ghosts[c]=new Point(i, j);
                    c++;
                }
            }
        }
//        gdx[0]=-1;
//        gdy[0]=0;
//        gdx[1]=1;
//        gdy[1]=0;
//        gdx[2]=0;
//        gdy[2]=1;
    }
    void drawPacman(Graphics g)
    {
        Graphics2D g2=(Graphics2D)g.create();
        if(left==true)
        {
            switch(currAnim)
            {
                case 0:
                    g2.drawImage(pacman, pacx, pacy, this); 
                    break;
                case 1:
                    g2.drawImage(p2left, pacx, pacy, this);
                    break;
                case 2:
                    g2.drawImage(p3left, pacx, pacy, this);
                    break;
                case 3:
                    g2.drawImage(p4left, pacx, pacy, this);
                    break;
            }
        }
        else if(right==true)
        {
            switch(currAnim)
            {
                case 0:
                    g2.drawImage(pacman, pacx, pacy, this); 
                      // System.out.println("in drawPacman pacx="+pacx+" pacy="+pacy);
                    break;
                case 1:
                    
                    g2.drawImage(p2right, pacx, pacy, this);
                    break;
                case 2:
                    g2.drawImage(p3right, pacx, pacy, this);
                    break;
                case 3:
                    g2.drawImage(p4right, pacx, pacy, this);
                    break;
            }
        }
        else if(up==true)
        {
            switch(currAnim)
            {
                case 0:
                    g2.drawImage(pacman, pacx, pacy, this); 
                    break;
                case 1:
                    g2.drawImage(p2up, pacx, pacy, this);
                    break;
                case 2:
                    g2.drawImage(p3up, pacx, pacy, this);
                    break;
                case 3:
                    g2.drawImage(p4up, pacx, pacy, this);
                    break;
            }
        }
        else
        {
            switch(currAnim)
            {
                case 0:
                    g2.drawImage(pacman, pacx, pacy, this); 
                    break;
                case 1:
                    g2.drawImage(p2down, pacx, pacy, this);
                    break;
                case 2:
                    g2.drawImage(p3down, pacx, pacy, this);
                    break;
                case 3:
                    g2.drawImage(p4down, pacx, pacy, this);
                    break;
            }
        }
            
    }
    void movePacman()
    {
        int prev_i,prev_j;
        prev_i=curr_i;
        prev_j=curr_j;
        curr_i=curr_i+dy;
        curr_j=curr_j+dx;
        if(curr_i>numOfBlocks-1)
            curr_i=0;
        if(curr_i<0)
            curr_i=numOfBlocks-1;
        if(curr_j>numOfBlocks-1)
            curr_j=0;
        if(curr_j<0)
            curr_j=numOfBlocks-1;
        //dx=dy=0;
        if(obstacles[curr_i][curr_j]==1)
        {
            curr_i=prev_i;
            curr_j=prev_j;
        }
        if(dots[curr_i][curr_j]==1)
        {
            dots[curr_i][curr_j]=0;
            noOfdots++;
        }
        pacx=curr_j*24;
        pacy=curr_i*24;
    }
    private int GDelay=2;
    private int GTimeElapsed=0;
    /*void moveGhosts()
    {
        int pac_block,nearestGhost=0;
        int allMinm=Integer.MAX_VALUE;
        int ndest_i=0,ndest_j=0;   
        int tmp_i=0,tmp_j=0;
       // Point[] tmp=new Po
        pac_block=curr_i*numOfBlocks+curr_j;
        if(GTimeElapsed>=GDelay)
        {
           GTimeElapsed=0;
           for(int i=0;i<numOfGhosts;i++)
           {
              int minm=Integer.MAX_VALUE;
              int g_i=ghosts[i].x,g_j=ghosts[i].y;
              ndest_i=g_i;
              ndest_j=g_j;
              if(g_i>0&&obstacles[g_i-1][g_j]==0)
              {
                  if(minm>shpath[(g_i-1)*numOfBlocks+g_j][pac_block])
                  {
                      ndest_i=g_i-1;
                      ndest_j=g_j;
                      minm=shpath[(g_i-1)*numOfBlocks+g_j][pac_block];
                  }
              }
            if(g_i==0&&obstacles[numOfBlocks-1][g_j]==0)
            {
                if(minm>shpath[(numOfBlocks-1)*numOfBlocks+g_j][pac_block])
                {
                    ndest_i=numOfBlocks-1;
                    ndest_j=g_j;
                    minm=shpath[(numOfBlocks-1)*numOfBlocks+g_j][pac_block];
                }
            }
            if(g_j>0&&obstacles[g_i][g_j-1]==0)
            {
                if(minm>shpath[g_i*numOfBlocks+g_j-1][pac_block])
                {
                    ndest_i=g_i;
                    ndest_j=g_j-1;
                    minm=shpath[g_i*numOfBlocks+g_j-1][pac_block];
                }
            }
            if(g_j==0&&obstacles[g_i][numOfBlocks-1]==0)
            {
                if(minm>shpath[(g_i)*numOfBlocks+numOfBlocks-1][pac_block])
                {
                    ndest_i=g_i;
                    ndest_j=numOfBlocks-1;
                    minm=shpath[(g_i)*numOfBlocks+numOfBlocks-1][pac_block];
                }
            }
            if(g_i<numOfBlocks-1&&obstacles[g_i+1][g_j]==0)
            {
                if(minm>shpath[(g_i+1)*numOfBlocks+g_j][pac_block])
                {
                    ndest_i=g_i+1;
                    ndest_j=g_j;
                    minm=shpath[(g_i+1)*numOfBlocks+g_j][pac_block];
                    
                }
            }
            if(g_i==numOfBlocks-1&&obstacles[0][g_j]==0)
            {
                if(minm>shpath[g_j][pac_block])
                {
                    ndest_i=0;
                    ndest_j=g_j;
                    minm=shpath[g_j][pac_block];
                }
            }
            if(g_j<numOfBlocks-1&&obstacles[g_i][g_j+1]==0)
            {
               if(minm>shpath[g_i*numOfBlocks+g_j+1][pac_block])
               {
                    ndest_i=g_i;
                    ndest_j=g_j+1;
                    minm=shpath[g_i*numOfBlocks+g_j+1][pac_block];
               }
            }
            if(g_j==numOfBlocks-1&&obstacles[g_i][0]==0)
            {
                if(minm>shpath[g_i*numOfBlocks][pac_block])
                {
                    ndest_i=g_i;
                    ndest_j=0;
                    minm=shpath[g_i*numOfBlocks][pac_block];
                }
            }
          //  ghosts[i].x=dest_i;
            //ghosts[i].y=dest_j;
            if(allMinm>minm)
            {
                nearestGhost=i;
                tmp_i=ndest_i;
                tmp_j=ndest_j;
                allMinm=minm;
            }
        }
        ghosts[nearestGhost].x=tmp_i;
        ghosts[nearestGhost].y=tmp_j;
//        for(int i=0;i<numOfGhosts;i++)
//        {
//              //int minm=Integer.MAX_VALUE;
//              int g_i=ghosts[i].x,g_j=ghosts[i].y;
//              int dest_i=g_i,dest_j=g_j;  
//              if(i==nearestGhost)
//                  continue;
//            if(g_i>0&&obstacles[g_i-1][g_j]==0)
//            {
//                   dest_i=g_i-1;
//                   dest_j=g_j;
//                   //continue; 
//            }
//            else if(g_i==0&&obstacles[numOfBlocks-1][g_j]==0)
//            {
//                dest_i=numOfBlocks-1;
//                dest_j=g_j;
//                //continue;
//            }
//            else if(g_j>0&&obstacles[g_i][g_j-1]==0)
//            {
//                 dest_i=g_i;
//                 dest_j=g_j-1;
//                 //continue;  
//            }
//            else if(g_j==0&&obstacles[g_i][numOfBlocks-1]==0)
//            {
//                dest_i=g_i;
//                dest_j=numOfBlocks-1;
//               // continue;
//            }
//            else if(g_i<numOfBlocks-1&&obstacles[g_i+1][g_j]==0)
//            {
//                dest_i=g_i+1;
//                dest_j=g_j;
//               // continue;
//            }
//            else if(g_i==numOfBlocks-1&&obstacles[0][g_j]==0)
//            {
//                dest_i=0;
//                dest_j=g_j;
//               // continue;
//            }
//            else if(g_j<numOfBlocks-1&&obstacles[g_i][g_j+1]==0)
//            {
//               dest_i=g_i;
//               dest_j=g_j+1;
//               //continue;
//            }
//            else if(g_j==numOfBlocks-1&&obstacles[g_i][0]==0)
//            {
//                dest_i=g_i;
//                dest_j=0;
//               // continue;
//            }
//            ghosts[i].x=dest_i;
//            ghosts[i].y=dest_j;
//        }   
          for(int i=0;i<numOfGhosts;i++)
           {
              int minm=Integer.MAX_VALUE;
              int g_i=ghosts[i].x,g_j=ghosts[i].y;
              ndest_i=g_i;
              ndest_j=g_j;
              if(g_i>0&&obstacles[g_i-1][g_j]==0)
              {
                  if(minm>shpath[(g_i-1)*numOfBlocks+g_j][pac_block])
                  {
                      ndest_i=g_i-1;
                      ndest_j=g_j;
                      minm=shpath[(g_i-1)*numOfBlocks+g_j][pac_block];
                  }
              }
            if(g_i==0&&obstacles[numOfBlocks-1][g_j]==0)
            {
                if(minm>shpath[(numOfBlocks-1)*numOfBlocks+g_j][pac_block])
                {
                    ndest_i=numOfBlocks-1;
                    ndest_j=g_j;
                    minm=shpath[(numOfBlocks-1)*numOfBlocks+g_j][pac_block];
                }
            }
            if(g_j>0&&obstacles[g_i][g_j-1]==0)
            {
                if(minm>shpath[g_i*numOfBlocks+g_j-1][pac_block])
                {
                    ndest_i=g_i;
                    ndest_j=g_j-1;
                    minm=shpath[g_i*numOfBlocks+g_j-1][pac_block];
                }
            }
            if(g_j==0&&obstacles[g_i][numOfBlocks-1]==0)
            {
                if(minm>shpath[(g_i)*numOfBlocks+numOfBlocks-1][pac_block])
                {
                    ndest_i=g_i;
                    ndest_j=numOfBlocks-1;
                    minm=shpath[(g_i)*numOfBlocks+numOfBlocks-1][pac_block];
                }
            }
            if(g_i<numOfBlocks-1&&obstacles[g_i+1][g_j]==0)
            {
                if(minm>shpath[(g_i+1)*numOfBlocks+g_j][pac_block])
                {
                    ndest_i=g_i+1;
                    ndest_j=g_j;
                    minm=shpath[(g_i+1)*numOfBlocks+g_j][pac_block];
                    
                }
            }
            if(g_i==numOfBlocks-1&&obstacles[0][g_j]==0)
            {
                if(minm>shpath[g_j][pac_block])
                {
                    ndest_i=0;
                    ndest_j=g_j;
                    minm=shpath[g_j][pac_block];
                }
            }
            if(g_j<numOfBlocks-1&&obstacles[g_i][g_j+1]==0)
            {
               if(minm>shpath[g_i*numOfBlocks+g_j+1][pac_block])
               {
                    ndest_i=g_i;
                    ndest_j=g_j+1;
                    minm=shpath[g_i*numOfBlocks+g_j+1][pac_block];
               }
            }
            if(g_j==numOfBlocks-1&&obstacles[g_i][0]==0)
            {
                if(minm>shpath[g_i*numOfBlocks][pac_block])
                {
                    ndest_i=g_i;
                    ndest_j=0;
                    minm=shpath[g_i*numOfBlocks][pac_block];
                }
            }
            ghosts[i].x=ndest_i;
            ghosts[i].y=ndest_j;
            /*if(allMinm>minm)
            {
                nearestGhost=i;
                tmp_i=ndest_i;
                tmp_j=ndest_j;
                allMinm=minm;
            }
        }
      }
      else
          GTimeElapsed+=1;
        return;
    }*/
    void moveGhosts()
    {
        int pac_block,nearestGhost=0;
        int allMinm=Integer.MAX_VALUE;
        int ndi=0,ndj=0;   
        int tmp_i=0,tmp_j=0;
       // Point[] tmp=new Po
        pac_block=curr_i*numOfBlocks+curr_j;
        if(GTimeElapsed>=GDelay)
        {
           GTimeElapsed=0;
           for(int i=0;i<numOfGhosts;i++)
           {
              int minm=Integer.MAX_VALUE;
              int g_i=ghosts[i].x,g_j=ghosts[i].y;
              if(g_i>0&&obstacles[g_i-1][g_j]==0)
              {
                  if(minm>shpath[(g_i-1)*numOfBlocks+g_j][pac_block])
                  {
                      ndi=-1;
                      ndj=0;
                      minm=shpath[(g_i-1)*numOfBlocks+g_j][pac_block];
                  }
              }
            if(g_i==0&&obstacles[numOfBlocks-1][g_j]==0)
            {
                if(minm>shpath[(numOfBlocks-1)*numOfBlocks+g_j][pac_block])
                {
                    ndi=-1;
                    ndj=0;
                    minm=shpath[(numOfBlocks-1)*numOfBlocks+g_j][pac_block];
                }
            }
            if(g_j>0&&obstacles[g_i][g_j-1]==0)
            {
                if(minm>shpath[g_i*numOfBlocks+g_j-1][pac_block])
                {
                    ndi=0;
                    ndj=-1;
                    minm=shpath[g_i*numOfBlocks+g_j-1][pac_block];
                }
            }
            if(g_j==0&&obstacles[g_i][numOfBlocks-1]==0)
            {
                if(minm>shpath[(g_i)*numOfBlocks+numOfBlocks-1][pac_block])
                {
                    ndi=0;
                    ndj=-1;
                    minm=shpath[(g_i)*numOfBlocks+numOfBlocks-1][pac_block];
                }
            }
            if(g_i<numOfBlocks-1&&obstacles[g_i+1][g_j]==0)
            {
                if(minm>shpath[(g_i+1)*numOfBlocks+g_j][pac_block])
                {
                    ndi=1;
                    ndj=0;
                    minm=shpath[(g_i+1)*numOfBlocks+g_j][pac_block];
                    
                }
            }
            if(g_i==numOfBlocks-1&&obstacles[0][g_j]==0)
            {
                if(minm>shpath[g_j][pac_block])
                {
                    ndi=1;
                    ndj=0;
                    minm=shpath[g_j][pac_block];
                }
            }
            if(g_j<numOfBlocks-1&&obstacles[g_i][g_j+1]==0)
            {
               if(minm>shpath[g_i*numOfBlocks+g_j+1][pac_block])
               {
                    ndi=0;
                    ndj=1;
                    minm=shpath[g_i*numOfBlocks+g_j+1][pac_block];
               }
            }
            if(g_j==numOfBlocks-1&&obstacles[g_i][0]==0)
            {
                if(minm>shpath[g_i*numOfBlocks][pac_block])
                {
                    ndi=0;
                    ndj=1;
                    minm=shpath[g_i*numOfBlocks][pac_block];
                }
            }
          //  ghosts[i].x=dest_i;
            //ghosts[i].y=dest_j;
            if(allMinm>minm)
            {
                nearestGhost=i;
                tmp_i=ndi;
                tmp_j=ndj;
                allMinm=minm;
            }
        }
        gdx[nearestGhost]=tmp_i;
        gdy[nearestGhost]=tmp_j;
        ghosts[nearestGhost].x+=tmp_i;
        ghosts[nearestGhost].y+=tmp_j;
        if(ghosts[nearestGhost].x<0)
            ghosts[nearestGhost].x=numOfBlocks-1;
        if(ghosts[nearestGhost].x>numOfBlocks-1)
            ghosts[nearestGhost].x=0;
        if(ghosts[nearestGhost].y<0)
            ghosts[nearestGhost].y=numOfBlocks-1;
        if(ghosts[nearestGhost].y>numOfBlocks-1)
            ghosts[nearestGhost].y=0;
        for(int i=0;i<numOfGhosts;i++)
        {
              //int minm=Integer.MAX_VALUE;
              int g_i=ghosts[i].x,g_j=ghosts[i].y;
              int di=0,dj=0;  
              if(i==nearestGhost)
                  continue;
            ArrayList<Point> arr=new ArrayList<>();
            if(g_i>0&&obstacles[g_i-1][g_j]==0)
            {
                   di=-1;
                   dj=0;
                   arr.add(new Point(di,dj));
                   //continue; 
            }
            if(g_i==0&&obstacles[numOfBlocks-1][g_j]==0)
            {
                di=-1;
                dj=0;
                arr.add(new Point(di,dj));
                //continue;
            }
            if(g_j>0&&obstacles[g_i][g_j-1]==0)
            {
                 di=0;
                 dj=-1;
                 arr.add(new Point(di,dj));
                 //continue;  
            }
            if(g_j==0&&obstacles[g_i][numOfBlocks-1]==0)
            {
                di=0;
                dj=-1;
                arr.add(new Point(di,dj));
               // continue;
            }
            if(g_i<numOfBlocks-1&&obstacles[g_i+1][g_j]==0)
            {
                di=1;
                dj=0;
                arr.add(new Point(di,dj));
               // continue;
            }
            if(g_i==numOfBlocks-1&&obstacles[0][g_j]==0)
            {
                di=1;
                dj=0;
                arr.add(new Point(di,dj));
               // continue;
            }
            if(g_j<numOfBlocks-1&&obstacles[g_i][g_j+1]==0)
            {
               di=0;
               dj=1;
               arr.add(new Point(di,dj));
               //continue;
            }
            if(g_j==numOfBlocks-1&&obstacles[g_i][0]==0)
            {
                di=0;
                dj=1;
                arr.add(new Point(di,dj));
               // continue;
            }
            int index;
            index=(int)(Math.random()*arr.size());
            ghosts[i].x+=arr.get(index).x;
            ghosts[i].y+=arr.get(index).y;
            if(ghosts[i].x<0)
                ghosts[i].x=numOfBlocks-1;
            if(ghosts[i].x>numOfBlocks-1)
                ghosts[i].x=0;
            if(ghosts[i].y<0)
                ghosts[i].y=numOfBlocks-1;
            if(ghosts[i].y>numOfBlocks-1)
                ghosts[i].y=0;
        }   
      }
      else
          GTimeElapsed+=1;
        return;
    }
    void navigateGhosts()
    {
        for(int i=0;i<numOfGhosts;i++)
        {
            ghosts[i].x+=gdx[i];
            ghosts[i].y+=gdy[i];
            if(ghosts[i].x<0)
                ghosts[i].x=numOfBlocks-1;
            if(ghosts[i].x>numOfBlocks-1)
                ghosts[i].x=0;
            if(ghosts[i].y<0)
                ghosts[i].y=numOfBlocks-1;
            if(ghosts[i].y>numOfBlocks-1)
            ghosts[i].y=0;
            
        }
    }
    void drawGhosts(Graphics2D g)
    {
       for(int i=0;i<numOfGhosts;i++)
       {
           g.drawImage(ghost_images[i],ghosts[i].y*24,ghosts[i].x*24, this);
       }
    }
    class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e); //To change body of generated methods, choose Tools | Templates.
            //System.out.println("Key Pressed");
            int key=e.getKeyCode();
            if(initialDelay>=20&&endDelay<=0)
            if(key==KeyEvent.VK_LEFT)
            {
                left=true;
                right=up=down=false;
                if(curr_j>0)
                {
                    if(obstacles[curr_i][curr_j-1]==0)
                    {
                        dx=-1;
                        dy=0;
                    }
                    else
                    {
                        dx=0;
                        dy=0;
                    }
                }
                else if(curr_j==0)
                {
                    if(obstacles[curr_i][numOfBlocks-1]==0)
                    {
                        dx=-1;
                        dy=0;
                    }
                    else
                    {
                        dx=0;
                        dy=0;
                    }
                }     
            }
            else if(key==KeyEvent.VK_RIGHT)
            {
                right=true;
                //System.out.println("Right Key Pressed");
                left=up=down=false;
                if(curr_j<numOfBlocks-1)
                {
                    if(obstacles[curr_i][curr_j+1]==0)
                    {
                        dx=1;
                        dy=0;
                    }
                    else
                    {
                        dx=0;
                        dy=0;
                    }
                }
                else if(curr_j==numOfBlocks-1)
                {
                    if(obstacles[curr_i][0]==0)
                    {
                        dx=1;
                        dy=0;
                    }
                    else
                    {
                        dx=0;
                        dy=0;
                    }
                }     
            }
            else if(key==KeyEvent.VK_UP)
            {
                up=true;
                left=right=down=false;
                if(curr_i>0)
                {
                    if(obstacles[curr_i-1][curr_j]==0)
                    {
                        dx=0;
                        dy=-1;
                    }
                    else
                    {
                        dx=0;
                        dy=0;
                    }
                }
                else if(curr_i==0)
                {
                    if(obstacles[numOfBlocks-1][curr_j]==0)
                    {
                        dx=0;
                        dy=-1;
                    }
                    else
                    {
                        dx=0;
                        dy=0;
                    }
                }     
            }
            else if(key==KeyEvent.VK_DOWN)
            {
                down=true;
                left=right=up=false;
                if(curr_i<numOfBlocks-1)
                {
                    if(obstacles[curr_i+1][curr_j]==0)
                    {
                        dx=0;
                        dy=1;
                    }
                    else
                    {
                        dx=0;
                        dy=0;
                    }
                }
                else if(curr_i==numOfBlocks-1)
                {
                    if(obstacles[0][curr_j]==0)
                    {
                        dx=0;
                        dy=1;
                    }
                    else
                    {
                        dx=0;
                        dy=0;
                    }
                }                   
            }
            
        }
        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();
             if(initialDelay>=10&&endDelay<=0)
            if (key == Event.LEFT || key == Event.RIGHT
                    || key == Event.UP || key == Event.DOWN) {
                dx = 0;
                dy = 0;
            }
        }
        
    }
    
}
