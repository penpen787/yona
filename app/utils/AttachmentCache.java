package utils;

import models.Attachment;
import models.enumeration.ResourceType;
import models.resource.Resource;
import play.cache.Cache;

import java.util.List;

/**
 * Utility for caching attachments in a container {@link models.resource.Resource}.
 *
 * The key is made by the name of the type of the Resource and Resource's id.
 * i.e) resource.getType().name() + resource.getId()
 *
 * The value is a collection of attachments.
 *
 * @author Keeun Baik
 */
public class AttachmentCache {

    /**
     * Play's Cache API allows expiration time in seconds.
     *
     * @see {@link play.cache.Cache#set(String, Object, int)}
     */
    private static final int ONE_DAY = 60 * 60 * 24;

    /**
     * Find cached attachments with the key is generated by
     * combining {@code containerType} and {@code containerId}.
     *
     * @param containerType
     * @param containerId
     * @return found cached data or null if there is no cached data.
     */
    public static List<Attachment> get(ResourceType containerType, String containerId) {
        String cacheKey = containerType.name() + containerId;
        Object cachedData = Cache.get(cacheKey);
        if (cachedData != null) {
            return (List<Attachment>) cachedData;
        } else {
            return null;
        }
    }

    /**
     * Cache attachments with the key
     *
     * @param key The key should be generated by combining {@code containerType} and {@code containerId}.
     * @param list
     */
    public static void set(String key, List<Attachment> list) {
        Cache.set(key, list, ONE_DAY);
    }

    /**
     * Cache attachments with the key is generated by {@code container}
     *
     * @param container
     * @param list
     */
    public static void set(Resource container, List<Attachment> list) {
        Cache.set(cacheKey(container), list, ONE_DAY);
    }

    /**
     * Find cached attachments with the key is generated by {@code container}
     *
     * @param container
     * @return
     */
    public static List<Attachment> get(Resource container) {
        String cacheKey = cacheKey(container);
        Object cachedData = Cache.get(cacheKey);
        if (cachedData != null) {
            return (List<Attachment>) cachedData;
        } else {
            return null;
        }
    }

    private static String cacheKey(Resource container) {
        return container.getType().name() + container.getId();
    }

    /**
     * Remove cached attachments with the key is generated by {@code container}
     *
     * @param container
     */
    public static void remove(Resource container) {
        Cache.remove(cacheKey(container));
    }

    /**
     * Remove cache that contains the {@code attachment}
     *
     * @param attachment
     */
    public static void remove(Attachment attachment) {
        Cache.remove(attachment.containerType.name() + attachment.containerId);
    }
}
