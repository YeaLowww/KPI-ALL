import pika
import logging
import sys
import time

# Set up logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)
logger.addHandler(logging.StreamHandler(sys.stdout))

def callback(ch, method, properties, body):
    message = body.decode()
    logger.info(f"Received message: {message}")
    # Process the message here
    # For example, you can log it, save it to a database, etc.

def main():
    connection = None
    for _ in range(5):  # Retry up to 5 times
        try:
            connection = pika.BlockingConnection(pika.ConnectionParameters(host='rabbitmq'))
            break
        except pika.exceptions.AMQPConnectionError as e:
            logger.error(f"Connection failed, retrying in 5 seconds... ({e})")
            time.sleep(5)
    
    if connection is None:
        logger.error("Failed to connect to RabbitMQ after several attempts.")
        sys.exit(1)

    channel = connection.channel()
    channel.queue_declare(queue='student_updates')
    channel.basic_consume(queue='student_updates', on_message_callback=callback, auto_ack=True)

    logger.info('Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()

if __name__ == "__main__":
    main()