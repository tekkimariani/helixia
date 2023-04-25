/**
 * 
 */
package com.tekkimariani.helixia.glfw;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.tekkimariani.helixia.editor.MenuBar;
import com.tekkimariani.helixia.editor.PropertiesWindow;
import com.tekkimariani.helixia.editor.ViewportWindow;
import com.tekkimariani.helixia.scene.Scene;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.ImVec2;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiMouseCursor;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.type.ImBoolean;



/**
 * @author tekki mariani
 *
 */
public class ImGuiLayer {

    private long glfwWindow;

    // Mouse cursors provided by GLFW
    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];

    // LWJGL3 renderer (SHOULD be initialized)
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    
    //private GameViewWindow gameViewWindow = new GameViewWindow();
    private ViewportWindow viewportWindow;
    private PropertiesWindow propertiesWindow;
    private MenuBar menuBar;

    public ImGuiLayer(long glfwWindow) {
        this.glfwWindow = glfwWindow;
        
        
        
        this.viewportWindow = new ViewportWindow();
        this.propertiesWindow = new PropertiesWindow();
        this.menuBar = new MenuBar();     
    }

    // Initialize Dear ImGui.
    public void initImGui() {
        // IMPORTANT!!
        // This line is critical for Dear ImGui to work.
        ImGui.createContext();

        // ------------------------------------------------------------
        // Initialize ImGuiIO config
        final ImGuiIO io = ImGui.getIO();

        //io.setIniFilename(null); // We don't want to save .ini file
        io.setIniFilename("imgui.ini");
        //io.setConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);// Navigation with keyboard
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        

        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
        io.setBackendPlatformName("imgui_java_impl_glfw");

        // ------------------------------------------------------------
        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
        io.setKeyMap(keyMap);

        // ------------------------------------------------------------
        // Mouse cursors mapping
        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

        // ------------------------------------------------------------
        // GLFW callbacks to handle user input

        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
            
            //if (!io.getWantCaptureKeyboard()) {
            	Keyboard.keyCallback(w, key, scancode, action, mods);
            //}
        });

        glfwSetCharCallback(glfwWindow, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }
            
            // Redirect to Mouse
            // TODO: GameViewWindow should not be static. If we do more than one view
            // this will break;
            if (!io.getWantCaptureMouse() || viewportWindow.getWantCaptureMouse()) {
            	Mouse.mouseButtonCallback(w, button, action, mods);
            }
        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
            if (viewportWindow.getWantCaptureMouseScroll()) {
            	Mouse.mouseScrollCallback(w, xOffset, yOffset);
            }
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(glfwWindow, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(glfwWindow);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });

        // ------------------------------------------------------------
        // Fonts configuration
        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

        // FontAtlas is like the spritesheet but for all letters and numbers of the alphabet
        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

        // Add a default font, which is 'ProggyClean.ttf, 13px'
        //fontAtlas.addFontDefault();

        // Fonts merge example
//        fontConfig.setMergeMode(true); // When enabled, all fonts added with this config would be merged with the previously added font
//        fontConfig.setPixelSnapH(true);

        fontAtlas.addFontFromFileTTF("src/main/resources/asset/font/acephimere/Acephimere Bold.otf", 20, fontConfig);

//        fontConfig.setMergeMode(false);
//        fontConfig.setPixelSnapH(false);



        fontConfig.destroy(); // After all fonts were added we don't need this config more
        
        
        
        
        ///
        /*
         * You may modify the ImGui::GetStyle() main instance during initialization and before NewFrame().
         * During the frame, use ImGui::PushStyleVar(ImGuiStyleVar_XXXX)/PopStyleVar() to alter the main 
         * style values,and ImGui::PushStyleColor(ImGuiCol_XXX)/PopStyleColor() for colors.

         */
        //ImGuiStyle style = new ImGuiStyle();
        ImGui.getStyle().setWindowRounding(12.0f);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        //public static final int Text = 0;
        //public static final int TextDisabled = 1;
        /**
         * Background of normal windows
         */
        //public static final int WindowBg = 2;
        //ImGui.getStyle().setColor(ImGuiCol.WindowBg,0.25f, 0.25f, 0.25f, 1.0f);
        /**
         * Background of child windows
         */
        //public static final int ChildBg = 3;
        /**
         * Background of popups, menus, tooltips windows
         */
        //public static final int PopupBg = 4;
        //public static final int Border = 5;
        //public static final int BorderShadow = 6;
        /**
         * Background of checkbox, radio button, plot, slider, text input
         */
        //public static final int FrameBg = 7;
        //public static final int FrameBgHovered = 8;
        //public static final int FrameBgActive = 9;
        //public static final int TitleBg = 10;
        //public static final int TitleBgActive = 11;
        //public static final int TitleBgCollapsed = 12;
        //public static final int MenuBarBg = 13;
        //public static final int ScrollbarBg = 14;
        //public static final int ScrollbarGrab = 15;
        //public static final int ScrollbarGrabHovered = 16;
        //public static final int ScrollbarGrabActive = 17;
        //public static final int CheckMark = 18;
        //public static final int SliderGrab = 19;
        //public static final int SliderGrabActive = 20;
        //public static final int Button = 21;
        //public static final int ButtonHovered = 22;
        //public static final int ButtonActive = 23;
        /**
         * Header* colors are used for CollapsingHeader, TreeNode, Selectable, MenuItem
         */
        //public static final int Header = 24;
        //public static final int HeaderHovered = 25;
        //public static final int HeaderActive = 26;
        //public static final int Separator = 27;
        //public static final int SeparatorHovered = 28;
        //public static final int SeparatorActive = 29;
        //public static final int ResizeGrip = 30;
        //public static final int ResizeGripHovered = 31;
        //public static final int ResizeGripActive = 32;
        //public static final int Tab = 33;
        //public static final int TabHovered = 34;
        //public static final int TabActive = 35;
        //public static final int TabUnfocused = 36;
        //public static final int TabUnfocusedActive = 37;
        /**
         * Preview overlay color when about to docking something
         */
        //public static final int DockingPreview = 38;
        /**
         * Background color for empty node (e.g. CentralNode with no window docked into it)
         */
        //public static final int DockingEmptyBg = 39;
        //public static final int PlotLines = 40;
        //public static final int PlotLinesHovered = 41;
        //public static final int PlotHistogram = 42;
        //public static final int PlotHistogramHovered = 43;
        /**
         * Table header background
         */
        //public static final int TableHeaderBg = 44;
        /**
         * Table outer and header borders (prefer using Alpha=1.0 here)
         */
        //public static final int TableBorderStrong = 45;
        /**
         * Table inner borders (prefer using Alpha=1.0 here)
         */
        //public static final int TableBorderLight = 46;
        /**
         * Table row background (even rows)
         */
        //public static final int TableRowBg = 47;
        /**
         * Table row background (odd rows)
         */
        //public static final int TableRowBgAlt = 48;
        //public static final int TextSelectedBg = 49;
        //public static final int DragDropTarget = 50;
        /**
         * Gamepad/keyboard: current highlighted item
         */
        //public static final int NavHighlight = 51;
        /**
         * Highlight window when using CTRL+TAB
         */
        //public static final int NavWindowingHighlight = 52;
        /**
         * Darken/colorize entire screen behind the CTRL+TAB window list, when active
         */
        //public static final int NavWindowingDimBg = 53;
        /**
         * Darken/colorize entire screen behind a modal window, when one is active
         */
        //public static final int ModalWindowDimBg = 54;
        
        
        
        
        
        
        
        
        
        
        
        //ImGui.getStyle().setColor(ImGuiCol.WindowBg,0.25f, 0.25f, 0.25f, 1.0f);
        //ImGui.getStyle().setColor(ImGuiCol.TitleBgActive, 0.75f, 0.25f, 0.25f, 1.0f);
        
        imGuiGl3.init("#version 330 core");
    }

    public void update(float dt, Scene scene) {
        startFrame(dt);

        // Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
        ImGui.newFrame();
        setupDockSpace();
        scene.imgui();
        //ImGui.showDemoWindow();
        this.viewportWindow.imgui();
        this.propertiesWindow.update(dt, scene);
        this.propertiesWindow.imgui();
        this.menuBar.imgui();
        ImGui.end();
        ImGui.render();
       
        endFrame();
    }

    private void startFrame(final float deltaTime) {
    	/*
        // Get window properties and mouse position
		IntBuffer intBufferWidth = BufferUtils.createIntBuffer(1);
		IntBuffer intBufferHeight = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(glfwWindow, intBufferWidth, intBufferHeight);
        float[] winWidth = {Window.getWidth()};
        float[] winHeight = {Window.getHeight()};
        
  */      
        double[] mousePosX = {0};
        double[] mousePosY = {0};
        glfwGetCursorPos(glfwWindow, mousePosX, mousePosY);

        
        // We SHOULD call those methods to update Dear ImGui state for the current frame
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(Window.getWidth(), Window.getHeight());
        io.setDisplayFramebufferScale(1f, 1f);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(deltaTime);

        // Update the mouse cursor
        final int imguiCursor = ImGui.getMouseCursor();
        glfwSetCursor(glfwWindow, mouseCursors[imguiCursor]);
        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    private void endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        //imGuiGl3.render(ImGui.getDrawData());
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    // If you want to clean a room after yourself - do it by yourself
    private void destroyImGui() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }
    
    
    private void setupDockSpace() {
    	// I don't know what exactly is the | doing. Search say its bitwise or.
    	
    	
    	
    	ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
    	ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
    	
    	ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
    	ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);

    	int windowFlags =
    			  ImGuiWindowFlags.NoTitleBar
    			| ImGuiWindowFlags.MenuBar
    			| ImGuiWindowFlags.NoDocking
    			| ImGuiWindowFlags.NoCollapse
    			| ImGuiWindowFlags.NoResize
    			| ImGuiWindowFlags.NoMove
    			| ImGuiWindowFlags.NoBringToFrontOnFocus
    			| ImGuiWindowFlags.NoNavFocus;
    	
    	ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
    	ImGui.popStyleVar(2);
    	
    	
    	// Dockspace
    	ImGui.dockSpace(ImGui.getID("Dockspace"));
    }
    
    public PropertiesWindow getPropertiesWindow() {
        return this.propertiesWindow;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}