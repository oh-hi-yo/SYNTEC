import java.util.*;
import java.lang.Character;

public class Minesweeper {
    public static void main(String[] args) {
        Minesweeper ms = new Minesweeper();
        Scanner scan = new Scanner(System.in);
        boolean play_again = false; // ¬O§_­nÄ~Äòª±¤U¤@½ü¡A¦Ó¥B¬Oª±¤W¤@½üª±¹Lªº
        int choose = 0; // ¥Ø«e­nª±­ş¤@ºØ
        while(true)
        {
            if(play_again == false)
            {
                System.out.println("\t\t½ò¦a¹p\n" +
            "¿ï¾Ü¹Cª±Ãø«×¡G\n" +
            "1. Â²³æ¡G(¦a¹Ï¤j¤p¼e: 4, °ª: 4, ¦a¹p¼Æ: 2)\n" +
            "2. ¤¤µ¥¡G(¦a¹Ï¤j¤p¼e: 5, °ª: 5, ¦a¹p¼Æ: 3)\n" + 
            "3. §xÃø¡G(¦a¹Ï¤j¤p¼e: 5, °ª: 5, ¦a¹p¼Æ: 5)\n" +
            "4. ¦Û­q¡Gª±®a¦Û¦æ¿é¤J¦a¹Ï¤j¤p»P¦a¹p¼Æ¶q\n" +
            "0. Â÷¶}¹CÀ¸");
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
                System.out.println("½Ğ¿é¤J¦a¹Ïªº¼e : ");
                int x = scan.nextInt();
                System.out.println("½Ğ¿é¤J¦a¹Ïªº°ª : ");
                int y = scan.nextInt();
                System.out.println("½Ğ¿é¤J¦a¹Ïªº¦a¹p¼Æ¶q : ");
                int mine_number = scan.nextInt();
                while(mine_number > x * y)
                {
                    System.out.println("¦a¹p¼Æ¶q­n<=¦a¹Ïªº¼e*°ª"+ "(" + x * y + "), ½Ğ¦A¿é¤J¤@¦¸ : ");
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
                System.out.println("½Ğ¿é¤J0 ~ 4, ¦A¿é¤J¤@¦¸");
            }

            System.out.println("­n¦PÃø«×­«·s¶}©l¶Ü?\n" + 
            "­nªº¸Ü¿é¤J:yes ; ¤£­nªº¸Ü(ª½±µµ²§ô)¿é¤J:no ; ¦A¿ï¤@¦¸Ãø«×ªº¸ÜÀH·N¿é¤J");
            String user_play_again = scan.next();
            if("yes".equals(user_play_again)) // java¤º¯S¦³ªºstring¬Ûµ¥§P§Oªk¡A¦]¬°°O¾ĞÅéªºÃö«Y¡Areference to:https://blog.csdn.net/q5706503/article/details/82954200, https://www.itread01.com/content/1544938806.html
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
    private int mine_number = 0; // ¨C­Ó¦a¹Ïm¦³¦Û¤vªº¬µ¼u¼Æ
    // create random object
    Random random = new Random();

    public Minemap(int size_x, int size_y, int mine_num) {
        map = new Draw(size_x, size_y);
        // set Anchor with something you don't want to be modified
        map.addAnchor('A'); // ´¡ºX®æªº­È = 65
        map.addAnchor('Z'); // ¬µ¼uªº­È = 90
        // random your mine on your map
        this.mine_number = mine_num;
        int mine_number_temp = this.mine_number;
        while (mine_number_temp > 0) {
            // ¤¶©ó0(§t)©Mn(¤£§t)°°ÀH¾÷¡A§¡¤Ã¤À¥¬ªºint­È
            int x = random.nextInt(size_x);
            int y = random.nextInt(size_y);
            if (map.getPoint(x, y) != 90) // Á×§K­«½Æ
            {
                this.setBomb(x, y);
                mine_number_temp--;
            }
        }
        // ®i¥Ü¥Îµe¥¬
        display_map = new Draw(size_x, size_y);
        display_map.addAnchor('A'); // ´¡ºX®æªº­È = 65
        display_map.addAnchor('Z'); // ¬µ¼uªº­È = 90
        display_map.addAnchor('O'); // Ãz¬µªº­È = 79
        display_map.addAnchor('Q'); // Q¥Nªí­ì¥»¬O¬µ¼u¡A¦ıdisplay_map¤w¸g¼ĞºX¼m¤F
        display_map.filledAll('B'); // ¦aªO¶ñ'B', 'B' = 66
    }

    public void play()
    {
        Scanner sc = new Scanner(System.in);
        int count_round = 0;
        while (true) {
            System.out.println("´¡¡B©ŞºX½Ğ¿é¤J1, ½ò¦a¹p½Ğ¿é¤J2");
            int user_want_to = sc.nextInt();
            System.out.println("½Ğ¿é¤J¸ÓÂIªºx®y¼Ğ : ");
            int x = sc.nextInt();
            System.out.println("½Ğ¿é¤J¸ÓÂIªºy®y¼Ğ : ");
            int y = sc.nextInt();
            if (this.isFlag(x, y) && 2 == user_want_to) // ­Y³o®æ¬°ºX¼m¥B·Q½ò¥L¡A´N²¤¹L
            {
                count_round++;
                System.out.println("²Ä" + count_round + "¦^¦X:");
                this.showMap();
            } else {
                if (1 == user_want_to) {
                    count_round++;
                    System.out.println("²Ä" + count_round + "¦^¦X:");
                    this.user_setFlag(x, y);
                    this.showMap();
                } else if (2 == user_want_to) { // ¥u¦³³oÃä·|¦³win or lose
                    int now_status = this.touchEvent(x, y);
                    if (103 == now_status) {
                        count_round++;
                        System.out.println("²Ä" + count_round + "¦^¦X-¹CÀ¸µ²§ô¡G§A¿é¤F");
                        this.showMap();
                        break;// ½ò¨ì¬µ¼u§Yµ²§ô
                    } else {
                        if (this.user_checkToWin()) {
                            count_round++;
                            System.out.println("²Ä" + count_round + "¦^¦X-¹CÀ¸µ²§ô¡G§AÄ¹¤F");
                            this.showMap();
                            break;
                        }
                        count_round++;
                        System.out.println("²Ä" + count_round + "¦^¦X:");
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
        // if(status >= 0 && status <= 8){ /*³o¬O­ì¥ıªº ·N¥~ªº¦P®É¥]§t¤FªÅ¥Õ­È ¥i¯à»İ­n¦Û¦æ³B²z*/
        if (status > 48 && status <= 56) { /* ³o¬O·sªº ¶È¶È¥]§t¼Æ¦r´£¥Ü®æ */
            display_map.setPoint(x, y, (char) status);
            return 102;
        } else if (90 == status) { // ¬µ¼u = 90 = 'Z'
            display_map.setPoint(x, y, 'O');// Ãz¬µ = 79 = 'O'
            this.showAllBomb();
            return 103;
        } else if (48 == status) { // ­Y­ì¥ıªºmap¬O¦aªO¡A¨ä­È¬°48
            this.openMap(x, y, (char) status);
            return 104;
        } else // ¨ä¥L¨S¦b¦Ò¼{½d³ò
        {
            return 105;
        }
    }

    private boolean openMap(int x, int y, char s) {
        if (map.isAnchor(x, y)) // ¦¹¬°mapªº¬µ¼u¡AºX¼m¦bdisplay_map
        {
            return false;
        }
        this.openMapBy(x, y, map.getPoint(x, y), s);
        return true;
    }

    private boolean switchTo_inMinemap(int x, int y, int src, char dest) { // §PÂ_¥Ø«e³o­ÓÂI¬O¤£¬O¤@¶}©lªºsrc
        int map_Point = map.getPoint(x, y);
        if (map_Point == src) {
            map.setPoint(x, y, 'B'); // «Ü­«­n:map­n§ï­È¡A¤£µMmap¤ºªº­È¥Ã»·m¬°0¡A·|µL­­»¼°j
            this.removeFlag_and_setSomething(x, y, dest); // ­Y¬Oªºsrc¡A´N¥h§â(x, y)ªº­È§ï¦¨dest
            return true;
        } else if (map_Point > 48 && map_Point <= 56) {
            this.removeFlag_and_setSomething(x, y, (char) map_Point); // ­Y¬O¼Æ¦r´N§âdisplay_map³]¬°¼Æ¦r
            return false; // ¨ì¼Æ¦r´£¥Ü®æ´N¥i¥H°±¤F
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
                        this.openMapBy(j, i, src, dest); // »¼°j¥hÀË¬d
                    }
                }
            }
        }
    }

    private boolean checkToWin() {
        int sum = 0;
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWeight(); j++) {
                if (display_map.getPoint(j, i) == 65 || display_map.getPoint(j, i) == 66) // ºX¼m = A = 65, ¦aªO = B = 66
                {
                    sum++;
                }
            }
        }
        if (sum == this.mine_number) // ­YÁ`©Mµ¥©ó¬µ¼u¼Æ¡A´N¬Oª±®aÄ¹¤F¡A¤Ï¤§¿é¤F
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
                    if (display_map.getPoint(j, i) == 65) // ­ì¥»¬O¬µ¼u¡A¦ı¤w¸g¼ĞºX¼m¤F
                    {
                        this.removeFlag_and_setSomething(j, i, 'Q'); // Q¥Nªí­ì¥»¬O¬µ¼u¡A¦ıdisplay_map¤w¸g¼ĞºX¼m¤F
                    }
                    display_map.setPoint(j, i, (char) 90);
                }
            }
        }
    }

    private void removeFlag_and_setSomething(int x, int y, char c) {
        display_map.removeAnchor('A'); // ¥ı§âºX¼m±qanchor®³±¼¡A¤£µM§ï¤£¤F
        display_map.setPoint(x, y, c); // ³]¬°¥Ø«eªºªF¦è
        display_map.addAnchor('A');
    }

    private boolean setFlag(int x, int y) {
        if (display_map.getPoint(x, y) == 66)// ¦aªO¶ñ'B', 'B' = 66
        {
            display_map.setPoint(x, y, 'A'); // ºX¼m = 65 = 'A'
            return true;
        } else if (this.isFlag(x, y)) {
            this.removeFlag_and_setSomething(x, y, 'B'); // ¥ı²¾±¼anchor¦A³]¬°¦aªO
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
        else if (display_map.getPoint(x, y) == 65)// ºX¼m = 65 = 'A'
        {
            return true;
        } else {
            return false;
        }
    }

    private void setBomb(int x, int y) {
        // ­Y¦¨¥\³]¬°¦a¹p¡A´N§â¦a¹p©P³òªº¼Æ¦rm¥[1¡A»¡©ú¦a¹p¼Æ¼W¥[¡A³o¸Ì¬O°µ¼Æ¦r´£¥Ü®æ¸ò©ñ¬µ¼uªº¤u§@
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
    private int count = 0;// ¦s¥Ø«e¦³´X­Óµe¥¬
    private Draw[] draw_array = new Draw[10];
    // ­ì¦]¬Oªx«¬¥²¬O™W§O™W«¬¡A¦Ó¤£¯à¬O°ò¥»™W«¬¡]¥]¬A°ò¥»™W«¬ªº°ò¤¸­È¡^¡A¥B°²³]allAnchor¸Ì­±ªº¤¸¯À¤£¯à­«½Æ
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

    public boolean sumPoint(int x, int y, char c)// ¦pªGÂI(x,y)¦s¦b«h¹ïÂI(x,y)ªº²{¦³¼Æ­È¥[¤Wc
    {
        if (this.isOutSide(x, y)) {
            return false;
        } else if (isAnchor(x, y)) {
            return false;
        } else {
            int temp = c;
            if (this.is_Integer(temp))// ¬O¾ã¼Æ
            {
                this.canvas[y][x] += (temp - 48);
            } else {
                this.canvas[y][x] += temp;
            }

            if (this.is_Overflow(canvas[y][x]))// ­Yoverflow´N«O¦sasciiªº³Ì¤j­È255
            {
                this.canvas[y][x] = (char) 255;
                return true;
            } else {
                return true;
            }
        }
    }

    public boolean is_Overflow(int x) {
        if (x > 255)// ¶W¹L255´Noverflow¤F
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
            return true;// ­Y¤£¦s¦b(¶V¬É)«h¦^¶Çtrue
        if (x >= this.weight || y >= this.height)
            return true;
        return false;// (¨S¶V¬É)¦^¶Çfalse
    }

    public boolean drawSquare(int x1, int y1, int x2, int y2, char c) {
        // use `setPoint()` to complete
        if (isOutSide(x1, y1) || isOutSide(x2, y2)) {
            return false;
        } else {
            if (y1 <= y2 && x1 <= x2)// şÂI:¥ª¤W¨¤ , ²×ÂI:¥k¤U¨¤
            {
                for (int i = y1; i <= y2; i++) {
                    for (int j = x1; j <= x2; j++) {
                        this.setPoint(j, i, c);
                    }
                }
            } else if (y1 >= y2 && x1 >= x2)// şÂI:¥k¤U¨¤ , ²×ÂI:¥ª¤W¨¤
            {
                for (int i = y2; i <= y1; i++) {
                    for (int j = x2; j <= x1; j++) {
                        this.setPoint(j, i, c);
                    }
                }
            } else// y2 > y1 || x2 > x1
            {
                if (x2 > x1 && y2 < y1)// şÂI:¥ª¤U¨¤ , ²×ÂI:¥k¤W¨¤
                {
                    for (int i = y2; i <= y1; i++) {
                        for (int j = x1; j <= x2; j++) {
                            this.setPoint(j, i, c);
                        }
                    }
                }
                if (x2 < x1 && y2 > y1)// şÂI:¥k¤W¨¤ , ²×ÂI:¥ª¤U¨¤
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

    public void sumAround(int x, int y, char c)// ¹ïÂI(x,y)©P¾D¤£¥]¬AÂI(x,y)ªº¬Û¾F8­ÓÂI¥[¤Wc
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
            } else// ­Y¦³¸òallAnchor¸Ì­±¬Û¦Pªº¤¸¯À¡A´Nª½±µµ²§ô
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

    private boolean switchTo(int x, int y, int src, char dest) { // §PÂ_¥Ø«e³o­ÓÂI¬O¤£¬O¤@¶}©lªºsrc
        if (this.getPoint(x, y) == src)
            return this.setPoint(x, y, dest); // ­Y¬Oªºsrc¡A´N¥h§â(x, y)ªº­È§ï¦¨dest
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
                        this.filledInBy(j, i, src, dest); // »¼°j¥hÀË¬d
                    }
                }
            }
        }
    }

    public void filledIn(int x, int y, char c) {
        if (this.isAnchor(x, y))
            return;
        // ¨ú±o¤@¶}©lªºsrc¡A³o¸Ìªºsrc¨úªk´N¤£·|¦]¬°«á­±x,y·|ÅÜ¦ÓÅÜ¤F¡A¦]¬°»¼°j¬O¦bfilledInBy¸Ì­±°µ¡C
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
                if (this.is_Integer(this.getPoint(x, y)))// ¬O0~9
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
                if (this.is_Integer(this.getPoint(x, y)))// ¬O0~9
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