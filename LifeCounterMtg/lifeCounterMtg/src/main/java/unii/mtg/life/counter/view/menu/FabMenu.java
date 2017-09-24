package unii.mtg.life.counter.view.menu;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

import unii.mtg.life.counter.GameActivity;
import unii.mtg.life.counter.R;

public class FabMenu {
    private AppCompatActivity mContext;
    private List<SubActionButton> mActionButtonList;
    private Drawable mSubMenuDrawable;
    private Drawable mMenuIcon;

    public FabMenu(@NonNull AppCompatActivity context, @DrawableRes int subMenuBackground, @DrawableRes int menuIcon) {
        mContext = context;
        mActionButtonList = new ArrayList<>();
        mSubMenuDrawable = ContextCompat.getDrawable(mContext, subMenuBackground);
        mMenuIcon = ContextCompat.getDrawable(mContext, menuIcon);
    }


    public void addSubActionButton(@DrawableRes int iconDrawable, @NonNull View.OnClickListener action) {
        SubActionButton menuItem = createMenuItem(ContextCompat.getDrawable(mContext, iconDrawable), action);
        mActionButtonList.add(menuItem);
    }

    public void init() {
        FloatingActionMenu.Builder builder = new FloatingActionMenu.Builder(mContext);

        for (SubActionButton subItems : mActionButtonList) {
            builder.addSubActionView(subItems);
        }
        FloatingActionMenu actionMenu = builder
                .attachTo(createButtonActionMenu())
                .build();
    }


    private FloatingActionButton createButtonActionMenu() {
        ImageView icon = new ImageView(mContext); // Create an icon
        icon.setImageDrawable(mMenuIcon);

        return new FloatingActionButton.Builder(mContext)
                .setContentView(icon).setBackgroundDrawable(mSubMenuDrawable)
                .build();
    }

    private SubActionButton createMenuItem(Drawable drawable, View.OnClickListener action) {
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(mContext);
        ImageView itemIcon = new ImageView(mContext);
        itemIcon.setImageDrawable(drawable);
        SubActionButton item = itemBuilder.setContentView(itemIcon).setBackgroundDrawable(mSubMenuDrawable).build();
        itemIcon.setOnClickListener(action);

        return item;
    }
}
