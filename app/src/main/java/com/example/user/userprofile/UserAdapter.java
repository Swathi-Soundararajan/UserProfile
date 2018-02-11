package com.example.user.userprofile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<UserList> useritem;
    private Context context;
    public UserAdapter(List<UserList> useritem, Context context) {
        this.useritem = useritem;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        UserList listItem=useritem.get(position);
        holder.username.setText(listItem.getUname());
        holder.skills.setText(listItem.getSkills());

        if (listItem.getDp().isEmpty()) {
            holder.dp.setImageResource(R.drawable.user);
        } else{
            Picasso.with(context)
                    .load(listItem.getDp())
                    .transform( new CircleTransform())
                    .into(holder.dp);
        }
    }

    @Override
    public int getItemCount() {
        return useritem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public TextView skills;
        public ImageView dp;

        public ViewHolder(View itemView) {
            super(itemView);
            username=(TextView) itemView.findViewById(R.id.profname);
            skills=(TextView) itemView.findViewById(R.id.skills);
            dp=(ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
