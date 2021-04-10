/*******************************************************************************
 * This file is part of the Astraios Engine distribution <https://github.com/TheRealJoe24/Astraios-Engine>
 * Copyright (C) 2021 TheRealJoe24
 * 
 * Astraios Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.therealjoe24.astraios.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;

import com.therealjoe24.astraios.Display;
import com.therealjoe24.astraios.Input;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Stores individual elements
 * 
 * @author TheRealJoe24
 *
 */
public class Canvas extends CanvasElement {
    
    private int _canvasWidth;
    private int _canvasHeight;
    private long _vg;

    /**
     * Create Canvas Object
     * 
     */
    public Canvas() {
        super(0, 0, 1, 1);
        Display.AddResizeCallbackFunc(new Display.SkyGLDisplayResizeFunc() {
            public void invoke() {
                _canvasWidth = Display.getWidth();
                _canvasHeight = Display.getHeight();
            }
        });
        Input.AddCursorCallback(new Input.AstraiosInputCursorFunc() {
            @Override
            public void invoke(double xpos, double ypos, double deltaX, double deltaY) {
                SendEvent(CanvasElementEvent.ELEMENT_MOUSE_MOVE, xpos, ypos, _canvasWidth, _canvasHeight);
            }
        });
        Input.AddMouseButtonCallback(new Input.AstraiosInputMouseButtonFunc() {
            @Override
            public void invoke(double xpos, double ypos, int button, int action, int mods) {
                if (button == GLFW_MOUSE_BUTTON_LEFT) {
                    if (action == GLFW_PRESS) {
                        SendEvent(CanvasElementEvent.ELEMENT_MOUSE_DOWN, xpos, ypos, _canvasWidth, _canvasHeight);
                    } else if (action == GLFW_RELEASE) {
                        SendEvent(CanvasElementEvent.ELEMENT_MOUSE_UP, xpos, ypos, _canvasWidth, _canvasHeight);
                    }
                }
            }
        });
        _vg = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS | NanoVGGL3.NVG_STENCIL_STROKES);
    }

    /**
     * Add new element to element list
     * 
     * @param el element to add
     */
    public void AddElement(CanvasElement el) {
        AddChild(el);
        el.InitFromContext(_vg);
    }

    /**
     * Render the canvas
     * 
     */
    public void Render() {
        NanoVG.nvgBeginFrame(_vg, _canvasWidth, _canvasHeight, 1);
        RenderChildren(_canvasWidth, _canvasHeight, _vg);
        NanoVG.nvgEndFrame(_vg);
    }

    @Override
    protected void ReceiveEvent(CanvasElementEvent evt, double mouseX, double mouseY, int frameWidth, int frameHeight) {
        
    }

}