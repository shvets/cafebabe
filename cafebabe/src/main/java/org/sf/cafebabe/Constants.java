// Constants.java

package org.sf.cafebabe;

/**
 * Constants for CafeBabe application
 */
public interface Constants {
  public String CAFEBABE_NAME = "CafeBabe";
  public String VERSION       = "1.4";

  public String MAIN_FRAME_TITLE  = CAFEBABE_NAME + " ver. " + VERSION;
  public String PROPERTIES_NAME   = "." + CAFEBABE_NAME;
  public String JAR_NAME          = CAFEBABE_NAME + ".jar";
  public String HELP_ARCHIVE_NAME = "instructions.jar";

  public String AUTHOR = "Alexander G. Shvets";
  public String EMAIL  = "shvets_alexander@yahoo.com";

  String GOODBYE_MESSAGE =
         "\n   Thank you for using " + CAFEBABE_NAME +
                                      " (" + "version " + VERSION + ")." +
         "\n   Please send your feedbacks to <" + EMAIL + ">.";

  public String MAILCAP_FILE_NAME   = "META-INF/mailcap.project";
  public String MIMETYPES_FILE_NAME = "META-INF/mimetypes.project";

  public String ICONS_DIR = "Icons/";

  public String ICON_HIERARCHY_CLASS      = ICONS_DIR + "class.gif";
  public String ICON_HIERARCHY_INTERFACE  = ICONS_DIR + "i-face.gif";
  public String ICON_HIERARCHY_UNKNOWN    = ICONS_DIR + "unknown.gif";

  public String ICON_COLLAPSE         = ICONS_DIR + "tree1.gif";
  public String ICON_EXPAND           = ICONS_DIR + "tree2.gif";
  public String ICON_EXPAND_ALL       = ICONS_DIR + "tree3.gif";
  public String ICON_SEARCH           = ICONS_DIR + "eye.gif";
  public String ICON_INTEGRITY        = ICONS_DIR + "integrity.gif";
  public String ICON_TUTORIAL_OFF     = ICONS_DIR + "quest1.gif";
  public String ICON_TUTORIAL_ON      = ICONS_DIR + "quest2.gif";

  public String ICON_CLASS_FILE       = ICONS_DIR + "cafebabe2.gif";
  public String ICON_CLASS            = ICONS_DIR + "class.gif";
  public String ICON_FIELD            = ICONS_DIR + "field.gif";
  public String ICON_METHOD           = ICONS_DIR + "method.gif";
  public String ICON_IMETHOD          = ICONS_DIR + "imethod.gif";
  public String ICON_NAME_AND_TYPE    = ICONS_DIR + "nat.gif";
  public String ICON_STRING           = ICONS_DIR + "string.gif";
  public String ICON_UTF8             = ICONS_DIR + "utf8.gif";
  public String ICON_UNICODE          = ICONS_DIR + "unicode.gif";
  public String ICON_JUMP             = ICONS_DIR + "cond.gif";
  public String ICON_INT              = ICONS_DIR + "int.gif";
  public String ICON_LONG             = ICONS_DIR + "long.gif";
  public String ICON_FLOAT            = ICONS_DIR + "float.gif";
  public String ICON_DOUBLE           = ICONS_DIR + "double.gif";

  public String ICON_EDIT             = ICONS_DIR + "VCROpen.gif";
  public String ICON_ADD              = ICONS_DIR + "add.gif";
  public String ICON_REMOVE           = ICONS_DIR + "remove.gif";

  public String MAGIC_NUMBER_TEXT     = "Magic Number";
  public String MINOR_VERSION_TEXT    = "Minor Version";
  public String MAJOR_VERSION_TEXT    = "Major Version";
  public String ACCESS_FLAGS_TEXT     = "Access Flags";
  public String THIS_CLASS_TEXT       = "This Class";
  public String SUPER_CLASS_TEXT      = "Super Class";
  public String INTERFACES_TEXT       = "Interfaces";
  public String FIELDS_TEXT           = "Fields";
  public String METHODS_TEXT          = "Methods";
  public String CLASS_ATTRIBUTES_TEXT = "Class Attributes";
  public String BAD_ENTRY_TEXT        = "Bad Entry";
  public String CONSTANT_POOL_TEXT    = "Constant Pool";


  public String ICON_ZIP_FILE = ICONS_DIR + "zip.gif";

  public String ICON_OPEN_FOLDER          = ICONS_DIR + "Open.gif";
  public String ICON_CLOSED_FOLDER        = ICONS_DIR + "Folder.gif";

  public String ICON_HELP                 = ICONS_DIR + "Help.gif";

  public String ICON_SER_FILE         = ICONS_DIR + "serfile.gif";

  public String STREAM_MAGIC_TEXT       = "Stream magic";
  public String STREAM_VERSION_TEXT     = "Stream version";
  public String REFERENCE_TABLE_TEXT    = "Reference Table";
  public String CLASS_NAME_TEXT         = "Class name";
  public String SERIAL_VERSION_UID_TEXT = "Serial version UID";
  public String NONE_TEXT               = "None";

  public String ICON_MAIN_FRAME = ICONS_DIR + "cafebabe.gif";

  public String ICON_OPEN_FILE  = ICONS_DIR + "open_file.gif";
  public String ICON_SAVE_FILE  = ICONS_DIR + "save.gif";

  public String ICON_FACE1      = ICONS_DIR + "face1.jpg";
  public String ICON_FACE2      = ICONS_DIR + "face2.jpg";
  public String ICON_MAIL       = ICONS_DIR + "mail.gif";
  public String ICON_ABOUT      = ICONS_DIR + "cafebabe1.gif";
  public String ICON_HOUND      = ICONS_DIR + "hound.gif";

  public String ICON_CLASS_HOUND = ICONS_DIR + "hound.gif";

  public final String FILE_MENU_TEXT           = "File";
  public final String TASK_MENU_TEXT           = "Task";
  public final String WINDOW_MENU_TEXT         = "Window";
  public final String HELP_MENU_TEXT           = "Help";

  public final String NEW_ITEM_TEXT            = "New";
  public final String OPEN_ITEM_TEXT           = "Open...";
  public final String SAVE_AS_ITEM_TEXT        = "Save as...";
  public final String CLOSE_ITEM_TEXT          = "Close";
  public final String CHANGE_PLAF_ITEM_TEXT    = "Change Look and Feel";
  public final String CHANGE_THEME_ITEM_TEXT   = "Change Theme for Metal LAF";
  public final String EXIT_ITEM_TEXT           = "Exit";

  public final String CLASS_HOUND_ITEM_TEXT    = "Class Hound Task";

  public final String EMAIL_ITEM_TEXT          = "Send e-mail";
  public final String ABOUT_ITEM_TEXT          = "About a program";

  public final char FILE_MENU_SHORTCUT         = 'F';
  public final char TASK_MENU_SHORTCUT         = 'T';
  public final char WINDOW_MENU_SHORTCUT       = 'W';
  public final char HELP_MENU_SHORTCUT         = 'H';

  public final char NEW_ITEM_SHORTCUT          = 'N';
  public final char OPEN_ITEM_SHORTCUT         = 'O';
  public final char SAVE_AS_ITEM_SHORTCUT      = 'a';
  public final char CLOSE_ITEM_SHORTCUT        = 'C';
  public final char CHANGE_PLAF_ITEM_SHORTCUT  = 'L';
  public final char CHANGE_THEME_ITEM_SHORTCUT = 'T';
  public final char EXIT_ITEM_SHORTCUT         = 'E';

  public final char CLASS_HOUND_ITEM_SHORTCUT  = 'H';

  public final char EMAIL_ITEM_SHORTCUT        = 'S';
  public final char ABOUT_ITEM_SHORTCUT        = 'A';

  public final String NEW_ITEM_DESCR_TEXT      = "Create file";
  public final String OPEN_ITEM_DESCR_TEXT     = "Open existing file";
  public final String SAVE_AS_ITEM_DESCR_TEXT  = "Save file as";
  public final String CLOSE_ITEM_DESCR_TEXT    = "Close file";
  public final String EXIT_ITEM_DESCR_TEXT     = "Exit";

  public final String CLASS_HOUND_ITEM_DESCR_TEXT = "Class Hound Task";

  public final String EMAIL_ITEM_DESCR_TEXT   = "Direct mail to author";
  public final String ABOUT_ITEM_DESCR_TEXT   = "About a program";

  public String CONFIRMATION_STRING = "Confirmation";
  public String SAVE_FILE_QUESTION  = "Do you want to save file";

  public static int SEARCH_PLAIN           = 0;
  public static int SEARCH_CONST_POOL      = 1;
  public static int SEARCH_FIELD           = 2;
  public static int SEARCH_METHOD          = 3;
  public static int SEARCH_CLASS_ATTRIBUTE = 4;

}
