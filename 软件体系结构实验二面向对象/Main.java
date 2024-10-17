package 软件体系结构实验二.软件体系结构实验二面向对象;

public class Main {

    public static void main(String[] args) {

        Input input = new Input();
        input.input("D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\软件体系结构实验二输入输出文档\\input.txt");
        Shift shift = new Shift(input.getLineTxt());
        shift.shift();
        Alphabetizer alphabetizer = new Alphabetizer(shift.getKwicList());
        alphabetizer.sort();
        Output output = new Output(alphabetizer.getKwicList());
        output.output("D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\软件体系结构实验二输入输出文档\\output2.txt");

    }
}