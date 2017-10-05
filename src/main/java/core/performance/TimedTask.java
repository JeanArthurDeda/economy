package core.performance;

import core.seri.SeriConf;

import java.util.Stack;


public class TimedTask {
    protected static Stack<Task> mStack = new Stack<>();

    protected static class Task {
        protected long mTime;
        protected long mLastPrintTime;
        protected String mName;
        protected String mIndent;

        protected Task(String name, String indent){
            mName = name;
            mTime = System.currentTimeMillis();
            mLastPrintTime = mTime;
            mIndent = indent;
            System.out.println (String.format("          %s%s started", mIndent, mName));
        }

        protected void update (double ratio){
            long now = System.currentTimeMillis();
            if (now > mLastPrintTime + 3000){
                mLastPrintTime = now;
                long time = now - mTime;
                double sec = (double)time / 1000.0;
                double eta = sec / ratio;
                int perc = (int)(ratio * 100.0);
                System.out.println (String.format("%10.3f%s%s %3d%% - Eta %.3f", sec, mIndent, mName, perc, eta));
            }
        }

        protected void finish (){
            mTime = System.currentTimeMillis() - mTime;
            double sec = (double)mTime / 1000.0;
            System.out.println (String.format("%10.3f%s%s done", sec, mIndent, mName));
        }
    }

    public static Task start (String name){
        // generate the indent
        String indent = SeriConf.INDENT;
        for (Task task : mStack) {
            indent += SeriConf.INDENT;
        }

        // generate the task
        Task task = new Task(name, indent);
        mStack.push(task);

        return task;
    }

    public static void update (double ratio){
        Task task = mStack.peek();
        task.update(ratio);
    }

    public static void finish (){
        Task task = mStack.pop();
        task.finish();
    }

}
