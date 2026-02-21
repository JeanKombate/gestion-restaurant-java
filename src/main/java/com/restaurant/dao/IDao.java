/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restaurant.dao;

import java.util.List;
/**
 *
 * @author Kanlanfaï KOMBATE
 * @param <T>
 */
public interface IDao<T> {
    void create(T obj);
    T read(int id);
    void update(T obj);
    void delete(int id);
    List<T> getAll();
}