/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpp.jatakamu.mbeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.inject.Inject;

/**
 *
 * @author phmulaka
 */
@Named(value = "depend")
@SessionScoped
public class DependBean
    implements Serializable
{
    @Inject SimpleBean simpleBean;
    
    private String dept = "DEPT1";

    /**
     * Creates a new instance of DependBean
     */
    public DependBean()
    {
        System.out.println("creating new instance of DependBean " + hashCode());
    }

//    public SimpleBean getSimpleBean()
//    {
//        return simpleBean;
//    }
//
//    public void setSimpleBean(SimpleBean simpleBean)
//    {
//        System.out.println("Setting Simple Bean instance " + simpleBean.hashCode());
//        this.simpleBean = simpleBean;
//    }

    public String getDept()
    {
        System.out.println("Using Simple Bean instance " + simpleBean.hashCode() + " name = " + simpleBean.getName());
        return simpleBean.getName() + "_DEPT1";
    }

    public void setDept(String dept)
    {
        this.dept = dept;
    }
    
}
