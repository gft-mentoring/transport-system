package com.exercise.transportsystem.common.domain.queue;

import com.exercise.transportsystem.common.domain.queue.message.MessageLocationUpdate;

public interface LocationQueuePoster {

    void postLocationMessage(MessageLocationUpdate msg);
}
