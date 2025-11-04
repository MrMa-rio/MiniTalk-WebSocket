package com.marsn.minitalkwebsocket.adapter.configuration;

import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import com.google.protobuf.MessageLite;

@Component
public class ProtoMessageConverterSocket implements MessageConverter {

    @Override
    public Object fromMessage(Message<?> message, Class<?> targetClass) {
        if (MessageLite.class.isAssignableFrom(targetClass)) {
            try {
                byte[] payload = (byte[]) message.getPayload();
                var method = targetClass.getMethod("parseFrom", byte[].class);
                return method.invoke(null, payload);
            } catch (Exception e) {
                throw new MessageConversionException("Failed to parse protobuf", e);
            }
        }
        return null;
    }

    @Override
    public Message<?> toMessage(Object payload, MessageHeaders headers) {
        if (payload instanceof MessageLite) {
            return new org.springframework.messaging.support.GenericMessage<>(
                    ((MessageLite) payload).toByteArray(), headers
            );
        }
        throw new MessageConversionException("Invalid protobuf message: " + payload);
    }
}
