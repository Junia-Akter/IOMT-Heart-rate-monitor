def main():

    # read settings.json
    from lib.at_client.io_util import read_settings
    ssid, password, atSign = read_settings()
    del read_settings

    # connect to wifi
    print('Connecting to WiFi %s...' % ssid)
    from lib.wifi import init_wlan
    init_wlan(ssid, password)
    del ssid, password, init_wlan

    # authenticate into server
    from lib.at_client.at_client import AtClient
    atClient = AtClient(atSign, writeKeys=True)
    del AtClient
    atClient.pkam_authenticate(verbose=True)

    from  machine import ADC
    from machine import Pin
    import utime
    import math
    
    analog_value = ADC(28)
    old_reading = 0
    count = 0
    time_count = 0
    led = Pin(15, Pin.OUT)
    value = ''

    print('Reading heart rate, please place finger on the sensor.')

    led.off() # make sure LED is off before starting

    utime.sleep(5)

    while True:
        new_reading = analog_value.read_u16()

        # if the ADC value is less than 5000,
        # the user does not have their finger on the sensor
        if (new_reading < 5000):
            value = 'Error'
            print('Put finger on sensor before running.')
            break

        if (old_reading > new_reading):
            led.toggle()
            print('\U00002764') # print emoji heart to terminal, just for fun
            count += 1
            utime.sleep(0.1)
            led.toggle()
        
        print(' ') # print empty line if no heart beat is read
        
        # check for heart beat every 0.25 seconds
        utime.sleep(0.25)
        time_count += 0.25
        old_reading = new_reading

        # measure heart rate for 15 seconds
        if time_count >= 15:
            value = count * 2
            # heart rate is the number of (beats * 2) * 2, roughly 2 beats per count
            # we are counting two beats (systolic and diastolic) per cycle
            break

    # 'heartRateSensor' is the key name
    # `value` is the value you want to store into the server
    # this will write the value into the device's atServer as a key like "public:heartRateSensor@bob" with value `value`.
    data = atClient.put_public('heartRateSensor', str(value)) # `data` is the response from the server. You will usually get a number (which is the commitId).

if __name__ == '__main__':
    main()