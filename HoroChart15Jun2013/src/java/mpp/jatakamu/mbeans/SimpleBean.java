package mpp.jatakamu.mbeans;

import javax.inject.Named;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author phmulaka
 */
@Named(value = "simple")
@SessionScoped
public class SimpleBean
    implements Serializable
{
    private String name = "";
    private int id = 0;

    /**
     * Creates a new instance of SimpleBean
     */
    public SimpleBean()
    {
        System.out.println("creating new instance of SimpleBean" + hashCode());
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
