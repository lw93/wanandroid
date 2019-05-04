package com.xygit.note.notebook.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * ListView 适配器
 *
 * @author Created by xiuyaun
 * @time on 2019/2/28
 */

public abstract class BaseListAdapter<E, H extends BaseListHolder> extends BaseAdapter {

    private List<E> mList;
    //    private H mViewHolder;
    protected int mLayoutResId;
    private AbsListView mAdapterView;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    protected OnItemChildClickListener mOnItemChildClickListener;
    protected OnItemChildLongClickListener mOnItemChildLongClickListener;
    protected OnScrollListener onScrollListener;
    protected int visibleLastIndex = 0;   //最后的可视项索引
    protected int visibleItemCount;       // 当前窗口可见项总数

    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int itemsLastIndex = BaseListAdapter.this.getCount() - 1;//数据集最后一项的索引
            int lastIndex = itemsLastIndex + 1;//加上底部的loadMoreView项
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
                //如果是自动加载,可以在这里放置异步加载数据的代码  
            }
            if (null != onScrollListener) {
                onScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            BaseListAdapter.this.visibleItemCount = visibleItemCount;
            visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
            if (null != onScrollListener) {
                onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    };

    public BaseListAdapter(@LayoutRes int layoutResId, @Nullable List<E> data, AbsListView adapterView) {
        if (layoutResId == 0) {
            throw new IllegalStateException("layoutResId cannot set zearo!");
        }
        this.mLayoutResId = layoutResId;
        this.mAdapterView = adapterView;
        this.mAdapterView.setOnScrollListener(mOnScrollListener);
        this.mList = data == null ? new ArrayList<E>() : data;
    }

    public BaseListAdapter(@LayoutRes int layoutResId, AbsListView adapterView) {
        this(layoutResId, null, adapterView);
    }

    public AdapterView getAdapterView() {
        return mAdapterView;
    }

//    public BaseListHolder getAdapterHolder() {
//        return mViewHolder;
//    }

    public List<E> getList() {
        return mList;
    }

    public void setList(List<E> mList) {
        if (null != mList) {
            this.mList.clear();
            this.mList = null;
        }
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void clear() {
        if (null != mList) {
            this.mList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (null != mList) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * if you want to use subclass of BaseListHolder in the adapter,
     * you must override the method to create new ViewHolder.
     *
     * @param view view
     * @return new ViewHolder
     */
    @SuppressWarnings("unchecked")
    protected H createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        H k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (H) new BaseListHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (H) new BaseListHolder(view);
    }

    /**
     * try to create Generic K instance
     *
     * @param z
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    private H createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (H) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (H) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get generic parameter K
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseListHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H holder;
        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) mAdapterView.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mLayoutResId, parent, false);
            holder = createBaseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (H) convertView.getTag();
        }
        convert(holder, mList.get(position),position);
        return convertView;
    }

    protected abstract void convert(H holder, E item,int position);

    /**
     * Interface definition for a callback to be invoked when an itemchild in this
     * view has been clicked
     */
    public interface OnItemChildClickListener {
        /**
         * callback method to be invoked when an itemchild in this view has been click
         *
         * @param adapter
         * @param view     The view whihin the ItemView that was clicked
         * @param position The position of the view int the adapter
         */
        void onItemChildClick(BaseAdapter adapter, View view, int position);
    }


    /**
     * Interface definition for a callback to be invoked when an childView in this
     * view has been clicked and held.
     */
    public interface OnItemChildLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param adapter  this BaseQuickAdapter adapter
         * @param view     The childView whihin the itemView that was clicked and held.
         * @param position The position of the view int the adapter
         * @return true if the callback consumed the long click ,false otherwise
         */
        boolean onItemChildLongClick(BaseAdapter adapter, View view, int position);
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * view has been clicked and held.
     */
    public interface OnItemLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param adapter  the adpater
         * @param view     The view whihin the RecyclerView that was clicked and held.
         * @param position The position of the view int the adapter
         * @return true if the callback consumed the long click ,false otherwise
         */
        boolean onItemLongClick(BaseAdapter adapter, View view, int position);
    }


    /**
     * Interface definition for a callback to be invoked when an item in this
     * RecyclerView itemView has been clicked.
     */
    public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this RecyclerView has
         * been clicked.
         *
         * @param adapter  the adpater
         * @param view     The itemView within the RecyclerView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         */
        void onItemClick(BaseAdapter adapter, View view, int position);
    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an itemchild in View has
     * been  clicked
     *
     * @param listener The callback that will run
     */
    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an itemchild  in this View has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    public void setOnItemChildLongClickListener(OnItemChildLongClickListener listener) {
        mOnItemChildLongClickListener = listener;
    }


    /**
     * @return The callback to be invoked with an item in this RecyclerView has
     * been long clicked and held, or null id no callback as been set.
     */
    public final OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    /**
     * @return The callback to be invoked with an item in this RecyclerView has
     * been clicked and held, or null id no callback as been set.
     */
    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * @return The callback to be invoked with an itemchild in this RecyclerView has
     * been clicked, or null id no callback has been set.
     */
    @Nullable
    public final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    /**
     * @return The callback to be invoked with an itemChild in this RecyclerView has
     * been long clicked, or null id no callback has been set.
     */
    @Nullable
    public final OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return mOnItemChildLongClickListener;
    }

    /**
     * Interface definition for a callback to be invoked when the list or grid
     * has been scrolled.
     */
    public interface OnScrollListener {

        /**
         * The view is not scrolling. Note navigating the list using the trackball counts as
         * being in the idle state since these transitions are not animated.
         */
        public static int SCROLL_STATE_IDLE = 0;

        /**
         * The user is scrolling using touch, and their finger is still on the screen
         */
        public static int SCROLL_STATE_TOUCH_SCROLL = 1;

        /**
         * The user had previously been scrolling using touch and had performed a fling. The
         * animation is now coasting to a stop
         */
        public static int SCROLL_STATE_FLING = 2;

        /**
         * Callback method to be invoked while the list view or grid view is being scrolled. If the
         * view is being scrolled, this method will be called before the next frame of the scroll is
         * rendered. In particular, it will be called before any calls to
         * {@link Adapter#getView(int, View, ViewGroup)}.
         *
         * @param view        The view whose scroll state is being reported
         * @param scrollState The current scroll state. One of
         *                    {@link #SCROLL_STATE_TOUCH_SCROLL} or {@link #SCROLL_STATE_IDLE}.
         */
        public void onScrollStateChanged(AbsListView view, int scrollState);

        /**
         * Callback method to be invoked when the list or grid has been scrolled. This will be
         * called after the scroll has completed
         *
         * @param view             The view whose scroll state is being reported
         * @param firstVisibleItem the index of the first visible cell (ignore if
         *                         visibleItemCount == 0)
         * @param visibleItemCount the number of visible cells
         * @param totalItemCount   the number of items in the list adaptor
         */
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount);
    }

    public OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void addItem(E e) {
        if (null != mList) {
            mList.add(e);
            notifyDataSetChanged();
        }
    }

    public void addItems(List<E> array) {
        if (null != array) {
            if (null == mList) {
                mList = new ArrayList<>();
            }
            int dataSize = array.size();
            for (int i = 0; i < dataSize; i++) {
                if (null != mList) {
                    mList.add(array.get(i));
                }
            }
            notifyDataSetChanged();
        }
    }


    public void removeItem(int index) {
        if (null != mList) {
            mList.remove(index);
            notifyDataSetChanged();
        }
    }

    public void removeItem(E e) {
        if (null != mList) {
            mList.remove(e);
            notifyDataSetChanged();
        }
    }

    public void removeItemAt(int position) {
        if (null != mList) {
            mList.remove(position);
            notifyDataSetChanged();
        }
    }


    public void removeItems(List<E> array) {
        if (null != array) {
            for (int i = 0, size = array.size(); i < size; i++) {
                if (null != mList) {
                    mList.removeAll(array);
                }
            }
        }
        notifyDataSetChanged();
    }


    public void removeItemAt(int position, int size) {
        if (null != mList) {
            for (int i = 0; i < size; i++) {
                if (null != mList) {
                    mList.remove(position + i);
                }
            }
            notifyDataSetChanged();
        }
    }
}
