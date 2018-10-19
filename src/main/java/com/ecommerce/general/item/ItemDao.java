package com.ecommerce.general.item;

import com.ecommerce.general.comment.Comment;
import java.util.List;

public interface ItemDao {

    public boolean updateItem(Item item);

    public boolean addItem(Item item);

    public boolean deleteItem(long id);
    
    public int getLastItemId();

    public List<Comment> getItemComments(long id, String sort);

    public List<Item> getAllItems(String sort);

    public Item getApprovedItemById(long id);

    public List<Item> getAllApprovedItems(String sort);

    public List<Item> getTagItems(String tag, String sort);

    public Item getItemById(long id);

    public int getNumItems();

    public List<Item> getLatestItems(int num);

    public boolean approveItem(long id);
}
