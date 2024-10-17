package 软件体系结构实验二.软件体系结构实验二事件系统;

public class Main {

    public static void main(String[] args) {

        //创建主题
        KWICSubject kwicSubject = new KWICSubject();
        //创建观察者
        Input input = new Input("D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\软件体系结构实验二输入输出文档\\input.txt");
        Shift shift = new Shift(input.getLineTxt());
        Alphabetizer alphabetizer = new Alphabetizer(shift.getKwicList());
        Output output = new Output(alphabetizer.getKwicList(), "D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二" +
                "\\软件体系结构实验二输入输出文档\\output3.txt");

        // 将观察者加入主题
        kwicSubject.addObserver(input);
        kwicSubject.addObserver(shift);
        kwicSubject.addObserver(alphabetizer);
        kwicSubject.addObserver(output);
        // 逐步调用各个观察者
        kwicSubject.startKWIC();
    }
}
