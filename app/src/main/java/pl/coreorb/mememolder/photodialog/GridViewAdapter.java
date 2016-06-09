package pl.coreorb.mememolder.photodialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.coreorb.mememolder.R;

/**
 * Adapter for select photo dialog's list.
 */
public class GridViewAdapter extends ArrayAdapter<Integer> {

    public GridViewAdapter(Context context, ArrayList<Integer> items) {
        super(context, R.layout.photo_dialog_grid_view_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.photo_dialog_grid_view_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.photo.setImageResource(getItem(position));

        return convertView;
    }


    static class ViewHolder {

        @BindView(R.id.photo)
        ImageView photo;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
