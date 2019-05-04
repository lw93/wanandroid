package com.xygit.note.notebook.manager.other;

import com.xygit.note.notebook.api.vo.TodoDesc;

import java.util.Comparator;

/**
 * todo 排序
 *
 * @author Created by xiuyaun
 * @time on 2019/4/14
 */

public class TodoDescComparator implements Comparator<TodoDesc> {
    @Override
    public int compare(TodoDesc o1, TodoDesc o2) {
        return (int) (o2.getDate() - o1.getDate());
    }
}
