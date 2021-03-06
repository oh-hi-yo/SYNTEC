import java.util.*;
import java.lang.Character;

public class Minesweeper {
    public static void main(String[] args) {
        Minesweeper ms = new Minesweeper();
        Scanner scan = new Scanner(System.in);
        boolean play_again = false; // 是否要繼續玩下一輪，而且是玩上一輪玩過的
        int choose = 0; // 目前要玩哪一種
        while(true)
        {
            if(play_again == false)
            {
                System.out.println("\t\t踩地雷\n" +
            "選擇遊玩難度：\n" +
            "1. 簡單：(地圖大小寬: 4, 高: 4, 地雷數: 2)\n" +
            "2. 中等：(地圖大小寬: 5, 高: 5, 地雷數: 3)\n" + 
            "3. 困難：(地圖大小寬: 5, 高: 5, 地雷數: 5)\n" +
            "4. 自訂：玩家自行輸入地圖大小與地雷數量\n" +
            "0. 離開遊戲");
                choose = scan.nextInt();
            }
            else
            {
                ;
            }

            Minemap now_game;
            if(1 == choose)
            {
                now_game = new Minemap(4, 4, 2);
                now_game.play();
            }
            else if(2 == choose)
            {
                now_game = new Minemap(5, 5, 3);
                now_game.play();    
            }
            else if(3 == choose)
            {
                now_game = new Minemap(5, 5, 5);
                now_game.play();
            }
            else if(4 == choose)
            {
                System.out.println("請輸入地圖的寬 : ");
                int x = scan.nextInt();
                System.out.println("請輸入地圖的高 : ");
                int y = scan.nextInt();
                System.out.println("請輸入地圖的地雷數量 : ");
                int mine_number = scan.nextInt();
                while(mine_number > x * y)
                {
                    System.out.println("地雷數量要<=地圖的寬*高"+ "(" + x * y + "), 請再輸入一次 : ");
                    mine_number = scan.nextInt();
                }
                now_game = new Minemap(x, y, mine_number);
                now_game.play();
            }
            else if(0 == choose)
            {
                System.out.println("Exit");
                break;
            }
            else
            {
                System.out.println("請輸入0 ~ 4, 再輸入一次");
            }

            System.out.println("要同難度重新開始嗎?\n" + 
            "要的話輸入:yes ; 不要的話(直接結束)輸入:no ; 再選一次難度的話隨意輸入");
            String user_play_again = scan.next();
            if("yes".equals(user_play_again)) // java內特有的string相等判別法，因為記憶體的關係，reference to:https://blog.csdn.net/q5706503/article/details/82954200, https://www.itread01.com/content/1544938806.html
            {
                play_again = true;
            }
            else if("no".equals(user_play_again))
            {
                play_again = false;
                break;
            }
            else
            {
                play_again = false;
                ms.pressAnyKeyToContinue();
            }
        }
        scan.close();
    }

    public void pressAnyKeyToContinue()
    { 
        System.out.println("Press Enter key to continue...");
        try
        {
            System.in.read();
        }  
        catch(Exception e)
        {}  
    }
}

class Minemap {
    Draw map;
    Draw display_map;
    private int mine_number = 0; // 每個地圖�m有自己的炸彈數
    // create random object
    Random random = new Random();

    public Minemap(int size_x, int size_y, int mine_num) {
        map = new Draw(size_x, size_y);
        // set Anchor with something you don't want to be modified
        map.addAnchor('A'); // 插旗格的值 = 65
        map.addAnchor('Z'); // 炸彈的值 = 90
        // random your mine on your map
        this.mine_number = mine_num;
        int mine_number_temp = this.mine_number;
        while (mine_number_temp > 0) {
            // 介於0(含)和n(不含)偽隨機，均勻分布的int值
            int x = random.nextInt(size_x);
            int y = random.nextInt(size_y);
            if (map.getPoint(x, y) != 90) // 避免重複
            {
                this.setBomb(x, y);
                mine_number_temp--;
            }
        }
        // 展示用畫布
        display_map = new Draw(size_x, size_y);
        display_map.addAnchor('A'); // 插旗格的值 = 65
        display_map.addAnchor('Z'); // 炸彈的值 = 90
        display_map.addAnchor('O'); // 爆炸的值 = 79
        display_map.addAnchor('Q'); // Q代表原本是炸彈，但display_map已經標旗幟了
        display_map.filledAll('B'); // 地板填'B', 'B' = 66
    }

    public void play()
    {
        Scanner sc = new Scanner(System.in);
        int count_round = 0;
        while (true) {
            System.out.println("插、拔旗請輸入1, 踩地雷請輸入2");
            int user_want_to = sc.nextInt();
            System.out.println("請輸入該點的x座標 : ");
            int x = sc.nextInt();
            System.out.println("請輸入該點的y座標 : ");
            int y = sc.nextInt();
            if (this.isFlag(x, y) && 2 == user_want_to) // 若這格為旗幟且想踩他，就略過
            {
                count_round++;
                System.out.println("第" + count_round + "回合:");
                this.showMap();
            } else {
                if (1 == user_want_to) {
                    count_round++;
                    System.out.println("第" + count_round + "回合:");
                    this.user_setFlag(x, y);
                    this.showMap();
                } else if (2 == user_want_to) { // 只有這邊會有win or lose
                    int now_status = this.touchEvent(x, y);
                    if (103 == now_status) {
                        count_round++;
                        System.out.println("第" + count_round + "回合-遊戲結束：你輸了");
                        this.showMap();
                        break;// 踩到炸彈即結束
                    } else {
                        if (this.user_checkToWin()) {
                            count_round++;
                            System.out.println("第" + count_round + "回合-遊戲結束：你贏了");
                            this.showMap();
                            break;
                        }
                        count_round++;
                        System.out.println("第" + count_round + "回合:");
                        this.showMap();
                    }
                } else {
                    ;
                }
            }
        }
    }


    public int touchEvent(int x, int y) {
        if (display_map.isOutSide(x, y))
            return 101;
        int status = map.getPoint(x, y);
        // if(status >= 0 && status <= 8){ /*這是原先的 意外的同時包含了空白值 可能需要自行處理*/
        if (status > 48 && status <= 56) { /* 這是新的 僅僅包含數字提示格 */
            display_map.setPoint(x, y, (char) status);
            return 102;
        } else if (90 == status) { // 炸彈 = 90 = 'Z'
            display_map.setPoint(x, y, 'O');// 爆炸 = 79 = 'O'
            this.showAllBomb();
            return 103;
        } else if (48 == status) { // 若原先的map是地板，其值為48
            this.openMap(x, y, (char) status);
            return 104;
        } else // 其他沒在考慮範圍
        {
            return 105;
        }
    }

    private boolean openMap(int x, int y, char s) {
        if (map.isAnchor(x, y)) // 此為map的炸彈，旗幟在display_map
        {
            return false;
        }
        this.openMapBy(x, y, map.getPoint(x, y), s);
        return true;
    }

    private boolean switchTo_inMinemap(int x, int y, int src, char dest) { // 判斷目前這個點是不是一開始的src
        int map_Point = map.getPoint(x, y);
        if (map_Point == src) {
            map.setPoint(x, y, 'B'); // 很重要:map要改值，不然map內的值永遠�m為0，會無限遞迴
            this.removeFlag_and_setSomething(x, y, dest); // 若是的src，就去把(x, y)的值改成dest
            return true;
        } else if (map_Point > 48 && map_Point <= 56) {
            this.removeFlag_and_setSomething(x, y, (char) map_Point); // 若是數字就把display_map設為數字
            return false; // 到數字提示格就可以停了
        } else {
            return false;
        }
    }

    private void openMapBy(int x, int y, int src, char dest) {
        if (this.switchTo_inMinemap(x, y, src, dest)) {
            for (int i = y - 1; i <= y + 1; i++) {
                for (int j = x - 1; j <= x + 1; j++) {
                    if (j == x && i == y) {
                        continue;
                    } else {
                        this.openMapBy(j, i, src, dest); // 遞迴去檢查
                    }
                }
            }
        }
    }

    private boolean checkToWin() {
        int sum = 0;
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWeight(); j++) {
                if (display_map.getPoint(j, i) == 65 || display_map.getPoint(j, i) == 66) // 旗幟 = A = 65, 地板 = B = 66
                {
                    sum++;
                }
            }
        }
        if (sum == this.mine_number) // 若總和等於炸彈數，就是玩家贏了，反之輸了
        {
            return true;
        } else {
            return false;
        }
    }

    public boolean user_checkToWin() {
        return this.checkToWin();
    }

    private void showAllBomb() {
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWeight(); j++) {
                if (map.getPoint(j, i) == 90) {
                    if (display_map.getPoint(j, i) == 65) // 原本是炸彈，但已經標旗幟了
                    {
                        this.removeFlag_and_setSomething(j, i, 'Q'); // Q代表原本是炸彈，但display_map已經標旗幟了
                    }
                    display_map.setPoint(j, i, (char) 90);
                }
            }
        }
    }

    private void removeFlag_and_setSomething(int x, int y, char c) {
        display_map.removeAnchor('A'); // 先把旗幟從anchor拿掉，不然改不了
        display_map.setPoint(x, y, c); // 設為目前的東西
        display_map.addAnchor('A');
    }

    private boolean setFlag(int x, int y) {
        if (display_map.getPoint(x, y) == 66)// 地板填'B', 'B' = 66
        {
            display_map.setPoint(x, y, 'A'); // 旗幟 = 65 = 'A'
            return true;
        } else if (this.isFlag(x, y)) {
            this.removeFlag_and_setSomething(x, y, 'B'); // 先移掉anchor再設為地板
            return true;
        } else {
            return false;
        }
    }

    public void user_setFlag(int x, int y) {
        this.setFlag(x, y);
    }

    public boolean isFlag(int x, int y) {
        if (display_map.isOutSide(x, y))
            return false;
        else if (display_map.getPoint(x, y) == 65)// 旗幟 = 65 = 'A'
        {
            return true;
        } else {
            return false;
        }
    }

    private void setBomb(int x, int y) {
        // 若成功設為地雷，就把地雷周圍的數字�m加1，說明地雷數增加，這裡是做數字提示格跟放炸彈的工作
        if (map.setPoint(x, y, 'Z')) {
            map.sumAround(x, y, (char) 1);
        }
    }

    public void showMapStatus() {
        map.renderInt();
    }

    public void showMap() {
        display_map.render();
    }
}

class Draw {
    private char[][] canvas = null;
    private int weight = -1;
    private int height = -1;
    private int count = 0;// 存目前有幾個畫布
    private Draw[] draw_array = new Draw[10];
    // 原因是泛型必是�W別�W型，而不能是基本�W型（包括基本�W型的基元值），且假設allAnchor裡面的元素不能重複
    ArrayList<Character> allAnchor = new ArrayList<Character>();

    public Draw(int w, int h) {
        if (count < 10) {
            this.createCanvas(w, h);
            this.filledAll('0');
            this.addDraw(this, 1);
        }
    }

    public int getHeight() {
        return this.height;
    }

    public int getWeight() {
        return this.weight;
    }

    public void addDraw(Draw newDraw, int index) {
        if (count < 10) {
            this.draw_array[index - 1] = newDraw;
            this.count++;
        }
    }

    public int get_Draw_Count() {
        return this.count;
    }

    public Draw get_DrawArray(int index) {
        return this.draw_array[index - 1];
    }

    public boolean createCanvas(int w, int h) {
        if (this.weight == -1 || this.height == -1) {
            this.weight = w;
            this.height = h;
            this.canvas = new char[height][weight];
            return true;
        }
        return false;
    }

    public void filledAll(char c) {
        // use for loop and `setPoint` function to complete
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.weight; j++) {
                this.setPoint(j, i, c);
            }
        }
    }

    public boolean setPoint(int x, int y, char c) {
        // check Whether `x` and `y` in canvas
        // outside return false
        if (this.isOutSide(x, y)) {
            return false;
        } else if (isAnchor(x, y)) {
            return false;
        } else// inside set c on canvas[y][x] and return true
        {
            this.canvas[y][x] = c;
            return true;
        }
    }

    public boolean sumPoint(int x, int y, char c)// 如果點(x,y)存在則對點(x,y)的現有數值加上c
    {
        if (this.isOutSide(x, y)) {
            return false;
        } else if (isAnchor(x, y)) {
            return false;
        } else {
            int temp = c;
            if (this.is_Integer(temp))// 是整數
            {
                this.canvas[y][x] += (temp - 48);
            } else {
                this.canvas[y][x] += temp;
            }

            if (this.is_Overflow(canvas[y][x]))// 若overflow就保存ascii的最大值255
            {
                this.canvas[y][x] = (char) 255;
                return true;
            } else {
                return true;
            }
        }
    }

    public boolean is_Overflow(int x) {
        if (x > 255)// 超過255就overflow了
        {
            return true;
        } else {
            return false;
        }
    }

    public int getPoint(int x, int y) {
        if (this.isOutSide(x, y))
            return -1;
        else {
            return this.canvas[y][x];
        }
    }

    public boolean isOutSide(int x, int y) {
        if (x < 0 || y < 0)
            return true;// 若不存在(越界)則回傳true
        if (x >= this.weight || y >= this.height)
            return true;
        return false;// (沒越界)回傳false
    }

    public boolean drawSquare(int x1, int y1, int x2, int y2, char c) {
        // use `setPoint()` to complete
        if (isOutSide(x1, y1) || isOutSide(x2, y2)) {
            return false;
        } else {
            if (y1 <= y2 && x1 <= x2)// ��點:左上角 , 終點:右下角
            {
                for (int i = y1; i <= y2; i++) {
                    for (int j = x1; j <= x2; j++) {
                        this.setPoint(j, i, c);
                    }
                }
            } else if (y1 >= y2 && x1 >= x2)// ��點:右下角 , 終點:左上角
            {
                for (int i = y2; i <= y1; i++) {
                    for (int j = x2; j <= x1; j++) {
                        this.setPoint(j, i, c);
                    }
                }
            } else// y2 > y1 || x2 > x1
            {
                if (x2 > x1 && y2 < y1)// ��點:左下角 , 終點:右上角
                {
                    for (int i = y2; i <= y1; i++) {
                        for (int j = x1; j <= x2; j++) {
                            this.setPoint(j, i, c);
                        }
                    }
                }
                if (x2 < x1 && y2 > y1)// ��點:右上角 , 終點:左下角
                {
                    for (int i = y1; i <= y2; i++) {
                        for (int j = x2; j <= x1; j++) {
                            this.setPoint(j, i, c);
                        }
                    }
                }

            }
            return true;
        }

    }

    public void setAround(int x, int y, char c) {
        // use `setPoint()` to complete
        for (int i = y - 1; i <= y + 1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                if (j == x && i == y) {
                    continue;
                } else {
                    this.setPoint(j, i, c);
                }
            }
        }

    }

    public void sumAround(int x, int y, char c)// 對點(x,y)周遭不包括點(x,y)的相鄰8個點加上c
    {
        for (int i = y - 1; i <= y + 1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                if (j == x && i == y) {
                    continue;
                } else {
                    this.sumPoint(j, i, c);
                }
            }
        }
    }

    public void addAnchor(char c) {
        for (int i = 0; i < allAnchor.size(); i++) {
            if (c != allAnchor.get(i)) {
                continue;
            } else// 若有跟allAnchor裡面相同的元素，就直接結束
            {
                return;
            }
        }
        allAnchor.add(c);
    }

    public void removeAnchor(char c) {
        for (int i = 0; i < allAnchor.size(); i++) {
            if (c == allAnchor.get(i)) {
                allAnchor.remove(i);
                return;
            }
        }
    }

    public boolean isAnchor(int x, int y) {
        char test = (char) this.getPoint(x, y);
        for (int i = 0; i < allAnchor.size(); i++) {
            if (test == allAnchor.get(i)) {
                return true;
            }
        }
        return false;
    }

    private boolean switchTo(int x, int y, int src, char dest) { // 判斷目前這個點是不是一開始的src
        if (this.getPoint(x, y) == src)
            return this.setPoint(x, y, dest); // 若是的src，就去把(x, y)的值改成dest
        else
            return false;
    }

    private void filledInBy(int x, int y, int src, char dest) {
        if (this.switchTo(x, y, src, dest)) {
            for (int i = y - 1; i <= y + 1; i++) {
                for (int j = x - 1; j <= x + 1; j++) {
                    if (j == x && i == y) {
                        continue;
                    } else {
                        this.filledInBy(j, i, src, dest); // 遞迴去檢查
                    }
                }
            }
        }
    }

    public void filledIn(int x, int y, char c) {
        if (this.isAnchor(x, y))
            return;
        // 取得一開始的src，這裡的src取法就不會因為後面x,y會變而變了，因為遞迴是在filledInBy裡面做。
        this.filledInBy(x, y, this.getPoint(x, y), c);
    }

    public boolean is_Integer(int x) {
        if (x >= 48 && x <= 57) {
            return true;
        } else {
            return false;
        }
    }

    public void render() {
        System.out.print(" ");
        for (int i = 0; i < this.weight; i++)
            System.out.printf(" %d", i);
        System.out.println();
        for (int y = 0; y < this.height; y++) {
            System.out.print(y);
            for (int x = 0; x < weight; x++) {
                if (this.is_Integer(this.getPoint(x, y)))// 是0~9
                {
                    System.out.printf(" %c", this.getPoint(x, y));
                } else {
                    System.out.printf(" %c", this.getPoint(x, y));
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void renderInt() {
        System.out.print(" ");
        for (int i = 0; i < this.weight; i++)
            System.out.printf(" %3d", i);
        System.out.println();
        for (int y = 0; y < this.height; y++) {
            System.out.print(y);
            for (int x = 0; x < this.weight; x++) {
                if (this.is_Integer(this.getPoint(x, y)))// 是0~9
                {
                    System.out.printf(" %3c", this.getPoint(x, y));
                } else {
                    System.out.printf(" %3d", this.getPoint(x, y));
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}