package 软件体系结构实验二.软件体系结构实验二管道过滤;

import java.io.IOException;

public abstract class Filter {

    protected Pipe input;
    protected Pipe output;

    public Filter(Pipe input, Pipe output) {

        this.input = input;
        this.output = output;
    }
    protected abstract void transform() throws IOException;
}
