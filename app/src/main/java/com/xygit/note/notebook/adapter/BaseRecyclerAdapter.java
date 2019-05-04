package com.xygit.note.notebook.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/1
 */

public abstract class BaseRecyclerAdapter<E, H extends BaseRecyclerHolder> extends RecyclerView.Adapter<H> {

    private List<E> mList;
    protected int mLayoutResId;
    private RecyclerView mRecyclerView;
    protected Context context;
    protected BaseRecyclerAdapter.OnItemClickListener mOnItemClickListener;
    protected BaseRecyclerAdapter.OnItemLongClickListener mOnItemLongClickListener;
    protected BaseRecyclerAdapter.OnItemChildClickListener mOnItemChildClickListener;
    protected BaseRecyclerAdapter.OnItemChildLongClickListener mOnItemChildLongClickListener;
    protected BaseRecyclerAdapter.OnScrollListener onScrollListener;


    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (null != onScrollListener) {
                onScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (null != onScrollListener) {
                onScrollListener.onScroll(recyclerView, dx, dy);
            }
        }
    };

    public BaseRecyclerAdapter(@LayoutRes int layoutResId, @Nullable List<E> data, RecyclerView adapterView) {
        if (layoutResId == 0) {
            throw new IllegalStateException("layoutResId cannot set zearo!");
        }
        this.mLayoutResId = layoutResId;
        this.mRecyclerView = adapterView;
        this.context = adapterView.getContext();
        this.mRecyclerView.addOnScrollListener(mOnScrollListener);
        this.mList = data == null ? new ArrayList<E>() : data;
    }

    public BaseRecyclerAdapter(@LayoutRes int layoutResId, RecyclerView adapterView) {
        this(layoutResId, null, adapterView);
    }

    public RecyclerView getAdapterView() {
        return mRecyclerView;
    }


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
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createBaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(mLayoutResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final H holder, int position) {
        convert(holder, mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    protected E getItem(int position) {
        return mList.get(position);
    }

    protected int getItemIndex(E item) {
        return mList.indexOf(item);
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
            k = (H) new BaseRecyclerHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (H) new BaseRecyclerHolder(view);
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

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        H holder;
//        if (convertView == null) {
//            final LayoutInflater inflater = (LayoutInflater) mRecyclerView.getContext()
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(mLayoutResId, parent, false);
//            holder = createBaseViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (H) convertView.getTag();
//        }
//        convert(holder, mList.get(position));
//        return convertView;
//    }

    protected abstract void convert(H holder, E item, int position);

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
        void onItemChildClick(RecyclerView.Adapter adapter, View view, int position);
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
        boolean onItemChildLongClick(RecyclerView.Adapter adapter, View view, int position);
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
        boolean onItemLongClick(RecyclerView.Adapter adapter, View view, int position);
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
        void onItemClick(RecyclerView.Adapter adapter, View view, int position);
    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(@Nullable BaseRecyclerAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an itemchild in View has
     * been  clicked
     *
     * @param listener The callback that will run
     */
    public void setOnItemChildClickListener(BaseRecyclerAdapter.OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an item in this RecyclerView has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    public void setOnItemLongClickListener(BaseRecyclerAdapter.OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an itemchild  in this View has
     * been long clicked and held
     *
     * @param listener The callback that will run
     */
    public void setOnItemChildLongClickListener(BaseRecyclerAdapter.OnItemChildLongClickListener listener) {
        mOnItemChildLongClickListener = listener;
    }


    /**
     * @return The callback to be invoked with an item in this RecyclerView has
     * been long clicked and held, or null id no callback as been set.
     */
    public final BaseRecyclerAdapter.OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    /**
     * @return The callback to be invoked with an item in this RecyclerView has
     * been clicked and held, or null id no callback as been set.
     */
    public final BaseRecyclerAdapter.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * @return The callback to be invoked with an itemchild in this RecyclerView has
     * been clicked, or null id no callback has been set.
     */
    @Nullable
    public final BaseRecyclerAdapter.OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    /**
     * @return The callback to be invoked with an itemChild in this RecyclerView has
     * been long clicked, or null id no callback has been set.
     */
    @Nullable
    public final BaseRecyclerAdapter.OnItemChildLongClickListener getOnItemChildLongClickListener() {
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
        public void onScrollStateChanged(RecyclerView view, int scrollState);

        public void onScroll(RecyclerView recyclerView, int dx, int dy);
    }

    public BaseRecyclerAdapter.OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    public void setOnScrollListener(BaseRecyclerAdapter.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void addItem(E e) {
        if (null != mList) {
            int index = mList.size();
            mList.add(e);
            notifyItemInserted(index);
            notifyItemChanged(index);
        }
    }

    public void addItemsSilent(List<E> array) {
        if (null != array) {
            if (null == mList) {
                mList = new ArrayList<>();
            }
            mList.addAll(array);
        }
    }

    public void addItems(List<E> array) {
        if (null != array) {
            if (null == mList) {
                mList = new ArrayList<>();
            }
            int originalSize = mList.size(), dataSize = array.size();
            mList.addAll(array);
            notifyItemRangeInserted(originalSize, dataSize);
            notifyItemRangeChanged(originalSize, originalSize + dataSize);
        }
    }

    public void removeItem(int index) {
        if (null != mList) {
            mList.remove(index);
            notifyItemRemoved(index);
            notifyItemChanged(index);
        }
    }

    public void removeItem(E e) {
        if (null != mList) {
            int index = getItemIndex(e);
            mList.remove(index);
            notifyItemRemoved(index);
            notifyItemChanged(index);
        }
    }

    public void removeItemAt(int position) {
        if (null != mList) {
            mList.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position);
        }
    }


    public void removeItems(List<E> array) {
        if (null != array) {
            for (int i = 0, size = array.size(); i < size; i++) {
                if (null != mList) {
                    mList.remove(array.get(i));
                }
            }
            notifyDataSetChanged();
        }
    }

    public void removeItemAt(int position, int size) {
        if (null != mList) {
            for (int i = 0; i < size; i++) {
                if (null != mList) {
                    mList.remove(position + i);
                }
            }
            notifyItemRangeRemoved(position, size);
            notifyItemRangeChanged(position, size);
        }
    }
}
