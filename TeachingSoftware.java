package 软件体系结构实验二.软件体系结构实验二教学系统;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

public class TeachingSoftware extends JFrame {

    private JComboBox<String> architectureComboBox;
    private JTextArea resultTextArea;
    private JButton processButton;
    private JButton showCodeButton;
    private File selectedFile;

    // GUI
    public TeachingSoftware() {
        // 设置窗口标题和布局
        setTitle("经典软件体系结构教学软件");
        setLayout(new BorderLayout());

        // 创建选择体系结构的下拉框
        String[] architectures = {"主程序-子程序", "面向对象", "事件系统", "管道-过滤器"};
        architectureComboBox = new JComboBox<>(architectures);

        // 创建文本区域用于显示处理结果
        resultTextArea = new JTextArea(30, 80);
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        // 设置介绍文本
        String introductionText = "欢迎使用经典软件体系结构教学软件！\n" +
                "本教学软件主要是通过实现 KWIC(Key Word In Context)，Parnas (1972)索引系统 来体现四种软件体系结构的功能。\n" +
                "快通过上面的下拉框选择你想要了解的体系结构风格，并点击下面的处理文件或者显示代码与说明来体验吧！\n\n";
        resultTextArea.setText(introductionText); // 将介绍文本设置到文本区域


        // 创建按钮
        processButton = new JButton("处理文件");
        showCodeButton = new JButton("显示代码与说明");

        // 设置按钮的监听事件
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAndProcessFile();
            }
        });

        showCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCodeAndExplanation();
            }
        });

        // 创建面板并添加组件
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("选择体系结构风格:"));
        topPanel.add(architectureComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(processButton);
        buttonPanel.add(showCodeButton);

        // 将面板和文本区域添加到主界面
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 设置窗口关闭操作和大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // 居中显示窗口
        setVisible(true);
    }

    // 文件选择和处理功能
    private void selectAndProcessFile() {
        // 使用JFileChooser选择文件
        JFileChooser fileChooser = new JFileChooser();//创建一个图形化的文件选择对话框,浏览本地磁盘的文件和目录
        int returnValue = fileChooser.showOpenDialog(null);// null表示直到用户做出选择或关闭对话框后才会返回
        if (returnValue == JFileChooser.APPROVE_OPTION) {// 表示用户点击了“打开”或“确定”按钮，选择了一个文件
            selectedFile = fileChooser.getSelectedFile();

            // 调用模拟文件处理进度的方法
            simulateFileProcessing();
        } else {
            resultTextArea.setText("未选择任何文件。\n");
        }
    }

    // 模拟文件处理进度
    private void simulateFileProcessing(){
        String selectedArchitecture = (String) architectureComboBox.getSelectedItem();
        resultTextArea.append("正在使用 " + selectedArchitecture + " 处理文件 请稍后...\n");

        // 创建一个Timer来延迟2秒后执行处理进度模拟
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 使用SwingWorker来模拟文件处理的进度，只是模拟，并不是实际处理速度
                SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        for (int i = 0; i <= 100; i++) {
                            Thread.sleep(20); // 模拟处理时间
                            publish(i); // 发布当前进度
                        }
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<Integer> chunks) {
                        int progress = chunks.get(chunks.size() - 1); // 获取最新的进度值
                        resultTextArea.setText("处理进度: " + progress + "%\n"); // 替换之前内容，达到界面只有当前处理进度的效果
                    }

                    @Override
                    protected void done() {
                        // 在后台处理文件,逻辑就是通过反射执行选择的包下面的main方法
                        String selectedArchitecture = (String) architectureComboBox.getSelectedItem();
                        String className = getClassNameForArchitecture(selectedArchitecture);// 去返回选择的体系结构的包名
                        // 调用特定包下的 main 方法
                        try {
                            Class<?> cls = Class.forName(className);
                            Method mainMethod = cls.getMethod("main", String[].class);

                            // 创建参数，传递空数组
                            String[] args = {}; // 可以根据需要传递参数
                            mainMethod.invoke(null, (Object) args); // 调用静态的 main 方法
                        } catch (Exception e) {
                            resultTextArea.append("调用 main 方法时出错: " + e.getMessage() + "\n");
                        }
                        resultTextArea.append("文件处理完成！\n");

                        // 显示处理完成的文件内容
                        displayFileContent(selectedArchitecture);
                    }
                };

                // 启动进度模拟
                worker.execute();
            }
        });

        // 只执行一次，启动延迟2秒的任务
        timer.setRepeats(false);
        timer.start();
    }

    // 根据选择的体系结构风格返回对应类名
    private String getClassNameForArchitecture(String architecture) {
        switch (architecture) {
            case "主程序-子程序":
                return "软件体系结构实验二主程序子程序.Main"; // 主程序-子程序的Main类
            case "面向对象":
                return "软件体系结构实验二面向对象.Main"; // 面向对象的Main类
            case "事件系统":
                return "软件体系结构实验二事件系统.Main"; // 事件系统的Main类
            case "管道-过滤器":
                return "软件体系结构管道过滤器.Main"; // 管道-过滤器的Main类
            default:
                throw new IllegalArgumentException("未知的体系结构风格: " + architecture);// 其实用不上这个代码
        }
    }

    // 显示处理完成的文件内容
    private void displayFileContent(String type) {
        String file;// 路径名
        switch (type) {
            case "主程序-子程序":
                file = "D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\软件体系结构实验二输入输出文档\\output1.txt"; // 主程序-子程序的输出文件
                break;
            case "面向对象":
                file = "D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\软件体系结构实验二输入输出文档\\output2.txt";// 面向对象的输出文件
                break;
            case "事件系统":
                file = "D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\软件体系结构实验二输入输出文档\\output3.txt";// 事件系统的输出文件
                break;
            case "管道-过滤器":
                file = "D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\软件体系结构实验二输入输出文档\\output4.txt";// 管道-过滤器的输出文件
                break;
            default:
                throw new IllegalArgumentException("未知的体系结构风格");// 其实用不上这个代码
        }
        // 指定特定的文件路径
        File specificFile = new File(file); // 实际的文件路径

        // 检查指定的文件是否存在
        if (specificFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(specificFile))) {
                String line;
                resultTextArea.append("\n文件处理后的内容:\n");
                // 逐行读取文件内容并显示
                while ((line = reader.readLine()) != null) {
                    resultTextArea.append(line + "\n");
                }
                resultTextArea.append("文件在本地目录：" + file + "\n");
            } catch (Exception e) {
                resultTextArea.append("读取文件内容时出错: " + e.getMessage() + "\n");
            }
        } else {
            resultTextArea.append("文件不存在或无效。\n");
        }
    }

    // 显示代码与说明
    private void showCodeAndExplanation() {
        String selectedArchitecture = (String) architectureComboBox.getSelectedItem();
        String imagePath;
        String explanation;
        String packageName;
        switch (selectedArchitecture) {
            case "主程序-子程序":
                // 说明文字
                explanation = "主程序-子程序系统中，input方法主要是负责读文件中的内容，\n并临时存到程序的lineTxt列表中，" +
                        "output方法主要负责把处理好的数据写入到指定的文件中，\nshift方法主要任务是对一个文本进行分词，" +
                        "并通过不断调整单词的顺序产生不同的排列\n，最后将每个排列结果存入一个列表，alphabetizer 方法是对 kwicList 按照\n" +
                        "AlphabetizerComparator类中的排序的规则进行排序\n\n";
                resultTextArea.setText(explanation);

                // 展示图片
                imagePath = "D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\软件体系结构实验二输入输出文档\\主程序-子程序源程序代码结构.png"; // 图片路径
                displayImage(imagePath);

                packageName = "软件体系结构实验二主程序子程序";
                break;
            case "面向对象":
                // 说明文字
                explanation = "和主程序-子程序很像，主要就是把主程序-子程序的所有方法挑出来变成类，\n" +
                        "并通过实例化类创建对象来调用他们的方法，从而完成排序等等功能。\n\n";
                resultTextArea.setText(explanation);

                // 展示图片
                imagePath = "D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\软件体系结构实验二输入输出文档\\面向对象程序代码结构.png"; // 图片路径
                displayImage(imagePath);

                packageName = "软件体系结构实验二面向对象";
                break;
            case "事件系统":
                // 说明文字
                explanation = "前面模式中主要的Input、Output、Shift、Alphabetizer类都要实现Observer接口，\n" +
                        "从而实现接口自定义的toDo方法，那么每个类实现的toDo方法中的代码其实就是每个类应该干的职责，\n" +
                        "在Subject类里面主要有通知特定的观察者（也就是调用此观察者的toDo方法）、\n" +
                        "增加一个观察者和删除一个观察者的功能，然后通过子类KWICSubject来添加遍历调用各个观察者toDo方法，\n" +
                        "最后主程序创建KWICSubject对象，并将Input、Output、Shift、Alphabetizer类的对象都添加到主题中\n" +
                        "然后调用toDo方法，从而完成功能。\n\n";
                resultTextArea.setText(explanation);

                // 展示图片
                imagePath = "D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\软件体系结构实验二输入输出文档\\事件系统程序代码结构.png"; // 图片路径
                displayImage(imagePath);

                packageName = "软件体系结构实验二事件系统";
                break;
            case "管道-过滤器":
                explanation = "首先主要的就是要将Input、Shift、Alphabetizer、Output按顺序用管道“串”起来，\n" +
                        "那么落实到代码中，具体的就是在上面四个类中必须要写构造方法，参数的逻辑必须是这个类向内\n" +
                        "接收的输入和向外输出的内容（内容可能是管道，也可能是文件），那么特殊的就是Input类和\n" +
                        "Output类，Input类接收的输入是文件的内容，向管道中输出，Output类接收的输入是管道中\n" +
                        "的内容，向文件中输出，其他两个Shift、Alphabetizer类的输入输出都是在跟管道打交道，\n" +
                        "所以Shift、Alphabetizer类的构造方法中逻辑上输入参数就是上一个管道，逻辑上输出参数\n" +
                        "是下一个管道，这时创建三个用于链接的管道（Pipe类）对象，这样就保证了他们被管道（Pipe类）\n" +
                        "“串”起来了，然后Input、Shift、Alphabetizer、Output四个类业务上很相似，所以抽象出来\n" +
                        "一个Filter类，将相同的特征放在这个类里面，最后就优雅地实现了管道-过滤软件体系结构。";
                resultTextArea.setText(explanation);

                // 展示图片
                imagePath = "D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\软件体系结构实验二输入输出文档\\管道-过滤器程序代码结构.png"; // 图片路径
                displayImage(imagePath);

                packageName = "软件体系结构实验二管道过滤";
                break;
            default:
                throw new IllegalArgumentException("未知的体系结构风格");// 其实用不上这个代码
        }

        // 展示包下的所有代码
        displayPackageCode(packageName);
    }

    // 方法来展示本地磁盘的图片
    private void displayImage(String imagePath) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(imageIcon);
        JFrame imageFrame = new JFrame("源程序代码结构");
        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageFrame.add(imageLabel);
        imageFrame.pack();
        imageFrame.setLocationRelativeTo(null);
        imageFrame.setVisible(true);
    }

    // 方法来展示某个包下面的所有代码
    private void displayPackageCode(String packageName) {
        try {
            // 获取当前目录的路径
            String currentDirectory = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();

            // 拼接当前目录和文件夹名称（packageName）
            String packagePath = "D:\\Java\\代码\\privatetest\\src\\软件体系结构实验二\\" + packageName;
            File directory = new File(packagePath);
            System.out.println("Package Path: " + packagePath);
            // 检查该目录是否存在
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles((dir, name) -> name.endsWith(".java")); // 只列出.java文件

                if (files != null && files.length > 0) {
                    for (File file : files) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            resultTextArea.append("\n文件: " + file.getName() + "\n");
                            String line;
                            while ((line = reader.readLine()) != null) {
                                resultTextArea.append(line + "\n");
                            }
                            resultTextArea.append("\n"); // 为每个文件添加一个空行
                        } catch (IOException e) {
                            resultTextArea.append("读取文件时出错: " + e.getMessage() + "\n");
                        }
                    }
                } else {
                    resultTextArea.append("未找到该目录下的代码文件。\n");
                }
            } else {
                resultTextArea.append("目录不存在或无效: " + directory.getPath() + "\n");
            }
        } catch (Exception e) {
            resultTextArea.append("发生错误: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        // 创建并显示应用程序
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TeachingSoftware();
            }
        });
    }
}
