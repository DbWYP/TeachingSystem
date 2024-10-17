package 软件体系结构实验二.软件体系结构实验二事件系统;

public class KWICSubject extends Subject{

    public void startKWIC(){

        for (int i = 0;i<4;i++){

            super.notifyOneObserver(i);
        }
    }
}
