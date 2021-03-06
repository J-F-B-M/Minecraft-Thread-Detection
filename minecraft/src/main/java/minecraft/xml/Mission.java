//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.31 at 12:55:29 PM CEST 
//


package minecraft.xml;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ProjectMalmo.microsoft.com}About"/>
 *         &lt;element ref="{http://ProjectMalmo.microsoft.com}ModSettings" minOccurs="0"/>
 *         &lt;element ref="{http://ProjectMalmo.microsoft.com}ServerSection"/>
 *         &lt;element ref="{http://ProjectMalmo.microsoft.com}AgentSection" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="SchemaVersion" type="{http://www.w3.org/2001/XMLSchema}token" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "about",
        "modSettings",
        "serverSection",
        "agentSection"
})
@XmlRootElement(name = "Mission")
public class Mission {

    @XmlElement(name = "About", required = true)
    protected About about;
    @XmlElement(name = "ModSettings")
    protected ModSettings modSettings;
    @XmlElement(name = "ServerSection", required = true)
    protected ServerSection serverSection;
    @XmlElement(name = "AgentSection", required = true)
    protected List<AgentSection> agentSection;
    @XmlAttribute(name = "SchemaVersion")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String schemaVersion;

    /**
     * Gets the value of the about property.
     *
     * @return possible object is
     * {@link About }
     */
    public About getAbout() {
        return about;
    }

    /**
     * Sets the value of the about property.
     *
     * @param value allowed object is
     *              {@link About }
     */
    public void setAbout(About value) {
        this.about = value;
    }

    /**
     * Gets the value of the modSettings property.
     *
     * @return possible object is
     * {@link ModSettings }
     */
    public ModSettings getModSettings() {
        return modSettings;
    }

    /**
     * Sets the value of the modSettings property.
     *
     * @param value allowed object is
     *              {@link ModSettings }
     */
    public void setModSettings(ModSettings value) {
        this.modSettings = value;
    }

    /**
     * Gets the value of the serverSection property.
     *
     * @return possible object is
     * {@link ServerSection }
     */
    public ServerSection getServerSection() {
        return serverSection;
    }

    /**
     * Sets the value of the serverSection property.
     *
     * @param value allowed object is
     *              {@link ServerSection }
     */
    public void setServerSection(ServerSection value) {
        this.serverSection = value;
    }

    /**
     * Gets the value of the agentSection property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agentSection property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAgentSection().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AgentSection }
     */
    public List<AgentSection> getAgentSection() {
        if (agentSection == null) {
            agentSection = new ArrayList<AgentSection>();
        }
        return this.agentSection;
    }

    /**
     * Gets the value of the schemaVersion property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * Sets the value of the schemaVersion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSchemaVersion(String value) {
        this.schemaVersion = value;
    }

}
