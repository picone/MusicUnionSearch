package chien.com.musicunionsearch.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class SongViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView singer;

    public SongViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(android.R.id.text1);
        singer = itemView.findViewById(android.R.id.text2);
    }
}
