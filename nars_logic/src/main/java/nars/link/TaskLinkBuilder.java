package nars.link;

import nars.Memory;
import nars.bag.tx.BagActivator;
import nars.task.Sentence;
import nars.task.Task;

/** adjusts budget of items in a Bag. ex: merge */
public class TaskLinkBuilder extends BagActivator<Sentence,TaskLink> {

    TermLinkTemplate template;
    private Task task;
    public final Memory memory;
    private float forgetCycles;
    private long now;


    public TaskLinkBuilder(Memory m) {
        super();
        this.memory = m;
    }

    public void setTask(Task t) {
        this.task = t;
//        if (template == null)
//            setKey(TaskLink.key(TermLink.SELF, null, t));
//        else
//            setKey(TaskLink.key(template.type, template.index, t));
        setKey(t);
        setBudget(t.getBudget());
        this.forgetCycles = memory.durationToCycles(
                memory.taskLinkForgetDurations.floatValue()
        );
        this.now = memory.time();
    }

    @Override
    public long time() {
        return now;
    }

    @Override
    public float getForgetCycles() {
        return forgetCycles;
    }

    public Task getTask() {
        return task;
    }

    public void setTemplate(TermLinkTemplate template) {
        this.template = template;
    }



    @Override
    public TaskLink newItem() {
        final Task t = getTask();
        if (template == null)
            return new TaskLink(t, getBudget());
        else
            return new TaskLink(t, template, getBudget());
    }


    @Override
    public String toString() {
        if (template==null)
            return task.toString();
        else
            return template + " " + task;
    }
}
