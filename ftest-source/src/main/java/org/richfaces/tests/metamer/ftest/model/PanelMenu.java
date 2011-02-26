package org.richfaces.tests.metamer.ftest.model;

import static org.jboss.test.selenium.locator.Attribute.CLASS;
import static org.jboss.test.selenium.locator.Attribute.SRC;
import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;

import org.jboss.test.selenium.GuardRequest;
import org.jboss.test.selenium.JQuerySelectors;
import org.jboss.test.selenium.RequestTypeModelGuard.Model;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.AjaxSeleniumProxy;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.locator.ElementLocator;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;
import org.jboss.test.selenium.request.RequestType;
import org.jboss.test.selenium.utils.text.SimplifiedFormat;
import org.richfaces.PanelMenuMode;

public class PanelMenu extends AbstractModel<JQueryLocator> implements Model {

    AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();

    private ReferencedLocator<JQueryLocator> topItems = ref(root, "> .rf-pm-top-itm");
    private ReferencedLocator<JQueryLocator> topGroups = ref(root, "> .rf-pm-top-gr");
    private ReferencedLocator<JQueryLocator> anySelectedItem = ref(root, "div[class*=rf-pm][class*=-itm-sel]");
    private ReferencedLocator<JQueryLocator> anySelectedGroup = ref(root, "div[class*=rf-pm][class*=-gr-sel]");
    private ReferencedLocator<JQueryLocator> anyDisabledItem = ref(root, "div[class*=rf-pm-][class*=-itm-dis]");
    private ReferencedLocator<JQueryLocator> anyDisabledGroup = ref(root, "div[class*=rf-pm-][class*=-gr-dis]");

    PanelMenuMode groupMode;
    PanelMenuMode itemMode;

    public PanelMenu(JQueryLocator root) {
        super(root);
    }

    public void setGroupMode(PanelMenuMode groupMode) {
        this.groupMode = groupMode;
    }

    public void setItemMode(PanelMenuMode itemMode) {
        this.itemMode = itemMode;
    }

    public int getItemCount() {
        return selenium.getCount(topItems);
    }

    public int getGroupCount() {
        return selenium.getCount(topGroups);
    }

    public Iterable<Item> getAllItems() {
        return new ModelIterable<JQueryLocator, Item>(topItems.getAllOccurrences(), Item.class);
    }

    public Iterable<Group> getAllGroups() {
        return new ModelIterable<JQueryLocator, Group>(topGroups.getAllOccurrences(), Group.class);
    }

    public Item getItem(int index) {
        return new Item(topItems.getNthOccurence(index));
    }

    public Item getItemContains(String string) {
        return new Item(JQuerySelectors.append(topItems, SimplifiedFormat.format(":contains('{0}')", string)));
    }

    public Group getGroup(int index) {
        return new Group(topGroups.getNthOccurence(index));
    }

    public Group getGroupContains(String string) {
        return new Group(JQuerySelectors.append(topGroups, SimplifiedFormat.format(":contains('{0}')", string)));
    }

    public Item getAnyTopItem() {
        return new Item(topItems.getReferenced());
    }

    public Group getAnyTopGroup() {
        return new Group(topGroups.getReferenced());
    }

    public Item getAnySelectedItem() {
        return new Item(anySelectedItem.getReferenced());
    }

    public Group getAnySelectedGroup() {
        return new Group(anySelectedGroup.getReferenced());
    }

    public Item getAnyDisabledItem() {
        return new Item(anyDisabledItem.getReferenced());
    }

    public Group getAnyDisabledGroup() {
        return new Group(anyDisabledGroup.getReferenced());
    }

    public Group getAnyExpandedGroup() {
        return new Group(JQuerySelectors.append(topGroups, ":has(.rf-pm-hdr-exp)"));
    }

    public class Group extends AbstractModel<JQueryLocator> implements Model {
        ReferencedLocator<JQueryLocator> header = ref(root, "> div[class*=rf-pm-][class*=-gr-hdr]");
        ReferencedLocator<JQueryLocator> label = ref(header, "> table > tbody > tr > td[class*=rf-pm-][class*=-gr-lbl]");
        ReferencedLocator<JQueryLocator> leftIcon = ref(header,
            "> table > tbody > tr > td[class*=rf-pm-][class*=-gr-ico]");
        ReferencedLocator<JQueryLocator> rightIcon = ref(header,
            "> table > tbody > tr > td[class*=rf-pm-][class*=-gr-exp-ico]");

        private ReferencedLocator<JQueryLocator> content = ref(root, "> div[class*=rf-pm-][class*=gr-cnt]");
        private ReferencedLocator<JQueryLocator> items = ref(content, "> .rf-pm-itm");
        private ReferencedLocator<JQueryLocator> groups = ref(content, "> .rf-pm-gr");

        public Group(JQueryLocator root) {
            super(root);
        }

        public int getItemCount() {
            return selenium.getCount(items);
        }

        public int getGroupCount() {
            return selenium.getCount(groups);
        }

        public Iterable<Item> getAllItems() {
            return new ModelIterable<JQueryLocator, Item>(items.getAllOccurrences(), Item.class);
        }

        public Item getItem(int index) {
            return new Item(items.getNthOccurence(index));
        }

        public Item getItemContains(String string) {
            return new Item(JQuerySelectors.append(items, SimplifiedFormat.format(":contains('{0}')", string)));
        }

        public Iterable<Group> getAllGroups() {
            return new ModelIterable<JQueryLocator, Group>(groups.getAllOccurrences(), Group.class);
        }

        public Group getGroup(int index) {
            return new Group(groups.getNthOccurence(index));
        }

        public Group getGroupContains(String string) {
            return new Group(JQuerySelectors.append(groups, SimplifiedFormat.format(":contains('{0}')", string)));
        }

        public Item getAnyItem() {
            return new Item(items.getReferenced());
        }

        public Group getAnyGroup() {
            return new Group(groups.getReferenced());
        }

        public boolean isSelected() {
            return selenium.getAttribute(header.getAttribute(CLASS)).contains("-sel");
        }

        public boolean isExpanded() {
            return selenium.getAttribute(header.getAttribute(CLASS)).contains("-exp");
        }

        public boolean isCollapsed() {
            return selenium.getAttribute(header.getAttribute(CLASS)).contains("-colps");
        }

        public boolean isHovered() {
            return selenium.getAttribute(this.getAttribute(CLASS)).contains("-hov");
        }

        public boolean isDisabled() {
            return selenium.getAttribute(this.getAttribute(CLASS)).contains("-dis");
        }

        public boolean isVisible() {
            return selenium.isElementPresent(this) && selenium.isVisible(this);
        }

        public Icon getLeftIcon() {
            return new Icon(leftIcon.getReferenced());
        }

        public Icon getRightIcon() {
            return new Icon(rightIcon.getReferenced());
        }

        public Label getLabel() {
            return new Label(label.getReferenced());
        }

        public void toggle() {
            if (groupMode == null) {
                selenium.click(label);
            } else {
                new GuardRequest(getRequestTypeForMode(groupMode)) {
                    public void command() {
                        selenium.click(label);
                    }
                }.waitRequest();
            }
        }

        public class Icon extends PanelMenu.Icon {

            public Icon(JQueryLocator root) {
                super(root);
            }

            @Override
            public ElementLocator<JQueryLocator> getIcon() {
                return this.getChild(jq("div:visible[class*=rf-pm-ico-]"));
            }
        }
    }

    public class Item extends AbstractModel<JQueryLocator> implements Model {
        ReferencedLocator<JQueryLocator> label = ref(root, "> table > tbody > tr > td[class*=rf-][class*=-itm-lbl]");
        ReferencedLocator<JQueryLocator> leftIcon = ref(root, "> table > tbody > tr > td[class*=rf-][class*=-itm-ico]");
        ReferencedLocator<JQueryLocator> rightIcon = ref(root,
            "> table > tbody > tr > td[class*=rf-][class*=-itm-exp-ico]");

        public Item(JQueryLocator root) {
            super(root);
        }

        public boolean isSelected() {
            return selenium.getAttribute(this.getAttribute(CLASS)).contains("-sel");
        }

        public boolean isHovered() {
            return selenium.getAttribute(this.getAttribute(CLASS)).contains("-hov");
        }

        public boolean isDisabled() {
            return selenium.getAttribute(this.getAttribute(CLASS)).contains("-dis");
        }

        public boolean isVisible() {
            return selenium.isElementPresent(this) && selenium.isDisplayed(this);
        }

        public Icon getLeftIcon() {
            return new Icon(leftIcon.getReferenced());
        }

        public Icon getRightIcon() {
            return new Icon(rightIcon.getReferenced());
        }

        public Label getLabel() {
            return new Label(label.getReferenced());
        }

        public void select() {
            if (itemMode == null) {
                selenium.click(label);
            } else {
                new GuardRequest(getRequestTypeForMode(itemMode)) {
                    public void command() {
                        selenium.click(label);
                    }
                }.waitRequest();
            }
        }

        public void hover() {
            selenium.mouseOver(this);
        }

        public class Icon extends PanelMenu.Icon {

            public Icon(JQueryLocator root) {
                super(root);
            }

            @Override
            public ElementLocator<JQueryLocator> getIcon() {
                return this;
            }
        }
    }

    public abstract class Icon extends AbstractModel<JQueryLocator> {

        public Icon(JQueryLocator root) {
            super(root);
        }

        ReferencedLocator<JQueryLocator> img = ref(root, "> img");
        AttributeLocator<?> imgSrc = img.getAttribute(SRC);

        public abstract ElementLocator<JQueryLocator> getIcon();

        public boolean isTransparent() {
            return selenium.getAttribute(getIcon().getAttribute(CLASS)).contains("-transparent");
        }

        public boolean containsClass(String styleClass) {
            return selenium.getAttribute(getIcon().getAttribute(CLASS)).contains(styleClass);
        }

        public boolean isCustomURL() {
            return selenium.isElementPresent(img);
        }

        public String getCustomURL() {
            return selenium.getAttribute(imgSrc);
        }
    }

    public class Label extends AbstractModel<JQueryLocator> {

        public Label(JQueryLocator root) {
            super(root);
        }

    }

    public static RequestType getRequestTypeForMode(PanelMenuMode mode) {
        switch (mode) {
            case ajax:
                return RequestType.XHR;
            case server:
                return RequestType.HTTP;
            default:
                return RequestType.NONE;
        }
    }

}
