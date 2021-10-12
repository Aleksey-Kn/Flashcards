public class Pair<First, Second> {
    First first;
    Second second;

    Pair(First f, Second s){
        first = f;
        second = s;
    }

    public Second getSecond() {
        return second;
    }

    public First getFirst() {
        return first;
    }
}
