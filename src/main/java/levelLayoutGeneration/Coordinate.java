package levelLayoutGeneration;

import java.util.Objects;

public class Coordinate
{
    public final int i;
    public final int j;

    public Coordinate(int i, int j)
    {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return i == that.i && j == that.j;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(i, j);
    }

    @Override
    public String toString()
    {
        return "Coordinate{" +
                "i=" + i +
                ", j=" + j +
                '}';
    }
}