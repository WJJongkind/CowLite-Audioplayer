/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.util;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 *
 * @author Wessel
 */
public class SugarySyntax {

    public interface TargetedAction<T> {
        public void perform(T delegate);
    }
    
    public interface Tryable {
        public void block() throws Exception;
    }
    
    public interface NonVoidTryable<T> {
        public T block() throws Exception;
    }
    
    public static <T> T nilCoalesce(T primary, T secondary) {
        return primary != null ? primary : secondary;
    }
    
    public static <T> void unwrappedPerform(WeakReference<T> target, TargetedAction<T> action) {
        if(target == null) {
            return;
        }
        
        T strongDelegate = target.get();
        if(strongDelegate != null) {
            action.perform(strongDelegate);
        }
    }
    
    public static <T> void unwrappedPerform(Collection<WeakReference<T>> targets, TargetedAction<T> action) {
        for(WeakReference<T> target : targets) {
            unwrappedPerform(target, action);
        }
    }
    
    public static <T extends Comparable> T clamp(T value, T lowerBound, T upperBound) {
        if(value.compareTo(lowerBound) < 0) {
            return lowerBound;
        } else if(value.compareTo(upperBound) > 0) {
            return upperBound;
        } else {
            return value;
        }
    }
    
    public static int clamp(int value, int lowerBound, int upperBound) {
        if(value < lowerBound) {
            return lowerBound;
        } else if(value > upperBound) {
            return upperBound;
        } else {
            return value;
        }
    }
    
    public static double clamp(double value, double lowerBound, double upperBound) {
        if(value < lowerBound) {
            return lowerBound;
        } else if(value > upperBound) {
            return upperBound;
        } else {
            return value;
        }
    }
    
    public static int maxInt(int... values) {
        int max = Integer.MIN_VALUE;
        for(int value : values) {
            max = Math.max(max, value);
        }
        return max;
    }
    
    public static double maxDouble(double... values) {
        double max = Integer.MIN_VALUE;
        for(double value : values) {
            max = Math.max(max, value);
        }
        return max;
    }
    
    public static <T extends Comparable> T max(T... values) {
        if(values.length == 0) {
            return null;
        }
        
        T max = values[0];
        for(T value : values) {
            if(value.compareTo(max) > 0) {
                max = value;
            }
        }
        
        return max;
    }
    
    public static int minInt(int... values) {
        int min = Integer.MAX_VALUE;
        for(int value : values) {
            min = Math.min(min, value);
        }
        return min;
    }
    
    public static double minDouble(double... values) {
        double min = Double.MAX_VALUE;
        for(double value : values) {
            min = Math.max(min, value);
        }
        return min;
    }
    
    public static <T extends Comparable> T min(T... values) {
        if(values.length == 0) {
            return null;
        }
        
        T min = values[0];
        for(T value : values) {
            if(value.compareTo(min) < 0) {
                min = value;
            }
        }
        
        return min;
    }
    
    public static double sigma(double... values) {
        double sum = 0;
        for(double value : values) {
            sum += value;
        }
        return sum;
    }
    
    public static int sigma(int... values) {
        int sum = 0;
        for(int value : values) {
            sum += value;
        }
        return sum;
    }
    
    // TODO convert to doTry where possible
    public static void doTry(Tryable tryable) {
        try {
            tryable.block();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static <T> T doTry(T defaultValue, NonVoidTryable<T> tryable) {
        try {
            return tryable.block();
        } catch(Exception e) {
            return defaultValue;
        }
    }

    public static boolean isByte(String string) {
        try {
            Byte.parseByte(string);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static boolean isShort(String string) {
        try {
            Short.parseShort(string);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static Byte tryParseByte(String string) {
        try {
            return Byte.parseByte(string);
        } catch(Exception e) {
            return null;
        }
    }

    public static Short tryParseShort(String string) {
        try {
            return Short.parseShort(string);
        } catch(Exception e) {
            return null;
        }
    }

    public static Integer tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch(Exception e) {
            return null;
        }
    }

    public static Float tryParseFloat(String string) {
        try {
            return Float.parseFloat(string);
        } catch(Exception e) {
            return null;
        }
    }

    public static Double tryParseDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch(Exception e) {
            return null;
        }
    }
    
}
