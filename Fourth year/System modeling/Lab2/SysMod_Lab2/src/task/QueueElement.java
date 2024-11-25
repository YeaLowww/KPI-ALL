package task;
public class QueueElement implements Comparable<QueueElement> {
    public final Element element;
    public double priority;

    public QueueElement(Element element, double priority) {
        assert priority <= 0;

        this.element = element;
        this.priority = priority;
    }

    @Override
    public int compareTo(QueueElement o) {
        return -Double.compare(this.priority, o.element.getTnext());
    }
}
