import java.util.*;
import java.lang.Character;

public class Minesweeper {
    public static void main(String[] args) {
        Minesweeper ms = new Minesweeper();
        Scanner scan = new Scanner(System.in);
        boolean play_again = false; // �O�_�n�~�򪱤U�@���A�ӥB�O���W�@�����L��
        int choose = 0; // �ثe�n�����@��
        while(true)
        {
            if(play_again == false)
            {
                System.out.println("\t\t��a�p\n" +
            "��ܹC�����סG\n" +
            "1. ²��G(�a�Ϥj�p�e: 4, ��: 4, �a�p��: 2)\n" +
            "2. �����G(�a�Ϥj�p�e: 5, ��: 5, �a�p��: 3)\n" + 
            "3. �x���G(�a�Ϥj�p�e: 5, ��: 5, �a�p��: 5)\n" +
            "4. �ۭq�G���a�ۦ��J�a�Ϥj�p�P�a�p�ƶq\n" +
            "0. ���}�C��");
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
                System.out.println("�п�J�a�Ϫ��e : ");
                int x = scan.nextInt();
                System.out.println("�п�J�a�Ϫ��� : ");
                int y = scan.nextInt();
                System.out.println("�п�J�a�Ϫ��a�p�ƶq : ");
                int mine_number = scan.nextInt();
                while(mine_number > x * y)
                {
                    System.out.println("�a�p�ƶq�n<=�a�Ϫ��e*��"+ "(" + x * y + "), �ЦA��J�@�� : ");
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
                System.out.println("�п�J0 ~ 4, �A��J�@��");
            }

            System.out.println("�n�P���׭��s�}�l��?\n" + 
            "�n���ܿ�J:yes ; ���n����(��������)��J:no ; �A��@�����ת����H�N��J");
            String user_play_again = scan.next();
            if("yes".equals(user_play_again)) // java���S����string�۵��P�O�k�A�]���O���骺���Y�Areference to:https://blog.csdn.net/q5706503/article/details/82954200, https://www.itread01.com/content/1544938806.html
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
    private int mine_number = 0; // �C�Ӧa�ϐm���ۤv�����u��
    // create random object
    Random random = new Random();

    public Minemap(int size_x, int size_y, int mine_num) {
        map = new Draw(size_x, size_y);
        // set Anchor with something you don't want to be modified
        map.addAnchor('A'); // ���X�檺�� = 65
        map.addAnchor('Z'); // ���u���� = 90
        // random your mine on your map
        this.mine_number = mine_num;
        int mine_number_temp = this.mine_number;
        while (mine_number_temp > 0) {
            // ����0(�t)�Mn(���t)���H���A���ä�����int��
            int x = random.nextInt(size_x);
            int y = random.nextInt(size_y);
            if (map.getPoint(x, y) != 90) // �קK����
            {
                this.setBomb(x, y);
                mine_number_temp--;
            }
        }
        // �i�ܥεe��
        display_map = new Draw(size_x, size_y);
        display_map.addAnchor('A'); // ���X�檺�� = 65
        display_map.addAnchor('Z'); // ���u���� = 90
        display_map.addAnchor('O'); // �z������ = 79
        display_map.addAnchor('Q'); // Q�N��쥻�O���u�A��display_map�w�g�кX�m�F
        display_map.filledAll('B'); // �a�O��'B', 'B' = 66
    }

    public void play()
    {
        Scanner sc = new Scanner(System.in);
        int count_round = 0;
        while (true) {
            System.out.println("���B�޺X�п�J1, ��a�p�п�J2");
            int user_want_to = sc.nextInt();
            System.out.println("�п�J���I��x�y�� : ");
            int x = sc.nextInt();
            System.out.println("�п�J���I��y�y�� : ");
            int y = sc.nextInt();
            if (this.isFlag(x, y) && 2 == user_want_to) // �Y�o�欰�X�m�B�Q��L�A�N���L
            {
                count_round++;
                System.out.println("��" + count_round + "�^�X:");
                this.showMap();
            } else {
                if (1 == user_want_to) {
                    count_round++;
                    System.out.println("��" + count_round + "�^�X:");
                    this.user_setFlag(x, y);
                    this.showMap();
                } else if (2 == user_want_to) { // �u���o��|��win or lose
                    int now_status = this.touchEvent(x, y);
                    if (103 == now_status) {
                        count_round++;
                        System.out.println("��" + count_round + "�^�X-�C�������G�A��F");
                        this.showMap();
                        break;// ��쬵�u�Y����
                    } else {
                        if (this.user_checkToWin()) {
                            count_round++;
                            System.out.println("��" + count_round + "�^�X-�C�������G�AĹ�F");
                            this.showMap();
                            break;
                        }
                        count_round++;
                        System.out.println("��" + count_round + "�^�X:");
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
        // if(status >= 0 && status <= 8){ /*�o�O����� �N�~���P�ɥ]�t�F�ťխ� �i��ݭn�ۦ�B�z*/
        if (status > 48 && status <= 56) { /* �o�O�s�� �ȶȥ]�t�Ʀr���ܮ� */
            display_map.setPoint(x, y, (char) status);
            return 102;
        } else if (90 == status) { // ���u = 90 = 'Z'
            display_map.setPoint(x, y, 'O');// �z�� = 79 = 'O'
            this.showAllBomb();
            return 103;
        } else if (48 == status) { // �Y�����map�O�a�O�A��Ȭ�48
            this.openMap(x, y, (char) status);
            return 104;
        } else // ��L�S�b�Ҽ{�d��
        {
            return 105;
        }
    }

    private boolean openMap(int x, int y, char s) {
        if (map.isAnchor(x, y)) // ����map�����u�A�X�m�bdisplay_map
        {
            return false;
        }
        this.openMapBy(x, y, map.getPoint(x, y), s);
        return true;
    }

    private boolean switchTo_inMinemap(int x, int y, int src, char dest) { // �P�_�ثe�o���I�O���O�@�}�l��src
        int map_Point = map.getPoint(x, y);
        if (map_Point == src) {
            map.setPoint(x, y, 'B'); // �ܭ��n:map�n��ȡA���Mmap�����ȥû��m��0�A�|�L�����j
            this.removeFlag_and_setSomething(x, y, dest); // �Y�O��src�A�N�h��(x, y)���ȧ令dest
            return true;
        } else if (map_Point > 48 && map_Point <= 56) {
            this.removeFlag_and_setSomething(x, y, (char) map_Point); // �Y�O�Ʀr�N��display_map�]���Ʀr
            return false; // ��Ʀr���ܮ�N�i�H���F
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
                        this.openMapBy(j, i, src, dest); // ���j�h�ˬd
                    }
                }
            }
        }
    }

    private boolean checkToWin() {
        int sum = 0;
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWeight(); j++) {
                if (display_map.getPoint(j, i) == 65 || display_map.getPoint(j, i) == 66) // �X�m = A = 65, �a�O = B = 66
                {
                    sum++;
                }
            }
        }
        if (sum == this.mine_number) // �Y�`�M���󬵼u�ơA�N�O���aĹ�F�A�Ϥ���F
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
                    if (display_map.getPoint(j, i) == 65) // �쥻�O���u�A���w�g�кX�m�F
                    {
                        this.removeFlag_and_setSomething(j, i, 'Q'); // Q�N��쥻�O���u�A��display_map�w�g�кX�m�F
                    }
                    display_map.setPoint(j, i, (char) 90);
                }
            }
        }
    }

    private void removeFlag_and_setSomething(int x, int y, char c) {
        display_map.removeAnchor('A'); // ����X�m�qanchor�����A���M�藍�F
        display_map.setPoint(x, y, c); // �]���ثe���F��
        display_map.addAnchor('A');
    }

    private boolean setFlag(int x, int y) {
        if (display_map.getPoint(x, y) == 66)// �a�O��'B', 'B' = 66
        {
            display_map.setPoint(x, y, 'A'); // �X�m = 65 = 'A'
            return true;
        } else if (this.isFlag(x, y)) {
            this.removeFlag_and_setSomething(x, y, 'B'); // ������anchor�A�]���a�O
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
        else if (display_map.getPoint(x, y) == 65)// �X�m = 65 = 'A'
        {
            return true;
        } else {
            return false;
        }
    }

    private void setBomb(int x, int y) {
        // �Y���\�]���a�p�A�N��a�p�P�򪺼Ʀr�m�[1�A�����a�p�ƼW�[�A�o�̬O���Ʀr���ܮ��񬵼u���u�@
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
    private int count = 0;// �s�ثe���X�ӵe��
    private Draw[] draw_array = new Draw[10];
    // ��]�O�x�����O�W�O�W���A�Ӥ���O�򥻙W���]�]�A�򥻙W�����򤸭ȡ^�A�B���]allAnchor�̭����������୫��
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

    public boolean sumPoint(int x, int y, char c)// �p�G�I(x,y)�s�b�h���I(x,y)���{���ƭȥ[�Wc
    {
        if (this.isOutSide(x, y)) {
            return false;
        } else if (isAnchor(x, y)) {
            return false;
        } else {
            int temp = c;
            if (this.is_Integer(temp))// �O���
            {
                this.canvas[y][x] += (temp - 48);
            } else {
                this.canvas[y][x] += temp;
            }

            if (this.is_Overflow(canvas[y][x]))// �Yoverflow�N�O�sascii���̤j��255
            {
                this.canvas[y][x] = (char) 255;
                return true;
            } else {
                return true;
            }
        }
    }

    public boolean is_Overflow(int x) {
        if (x > 255)// �W�L255�Noverflow�F
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
            return true;// �Y���s�b(�V��)�h�^��true
        if (x >= this.weight || y >= this.height)
            return true;
        return false;// (�S�V��)�^��false
    }

    public boolean drawSquare(int x1, int y1, int x2, int y2, char c) {
        // use `setPoint()` to complete
        if (isOutSide(x1, y1) || isOutSide(x2, y2)) {
            return false;
        } else {
            if (y1 <= y2 && x1 <= x2)// ���I:���W�� , ���I:�k�U��
            {
                for (int i = y1; i <= y2; i++) {
                    for (int j = x1; j <= x2; j++) {
                        this.setPoint(j, i, c);
                    }
                }
            } else if (y1 >= y2 && x1 >= x2)// ���I:�k�U�� , ���I:���W��
            {
                for (int i = y2; i <= y1; i++) {
                    for (int j = x2; j <= x1; j++) {
                        this.setPoint(j, i, c);
                    }
                }
            } else// y2 > y1 || x2 > x1
            {
                if (x2 > x1 && y2 < y1)// ���I:���U�� , ���I:�k�W��
                {
                    for (int i = y2; i <= y1; i++) {
                        for (int j = x1; j <= x2; j++) {
                            this.setPoint(j, i, c);
                        }
                    }
                }
                if (x2 < x1 && y2 > y1)// ���I:�k�W�� , ���I:���U��
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

    public void sumAround(int x, int y, char c)// ���I(x,y)�P�D���]�A�I(x,y)���۾F8���I�[�Wc
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
            } else// �Y����allAnchor�̭��ۦP�������A�N��������
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

    private boolean switchTo(int x, int y, int src, char dest) { // �P�_�ثe�o���I�O���O�@�}�l��src
        if (this.getPoint(x, y) == src)
            return this.setPoint(x, y, dest); // �Y�O��src�A�N�h��(x, y)���ȧ令dest
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
                        this.filledInBy(j, i, src, dest); // ���j�h�ˬd
                    }
                }
            }
        }
    }

    public void filledIn(int x, int y, char c) {
        if (this.isAnchor(x, y))
            return;
        // ���o�@�}�l��src�A�o�̪�src���k�N���|�]���᭱x,y�|�ܦ��ܤF�A�]�����j�O�bfilledInBy�̭����C
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
                if (this.is_Integer(this.getPoint(x, y)))// �O0~9
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
                if (this.is_Integer(this.getPoint(x, y)))// �O0~9
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