# CatChat
Open source GroupMe chat client.

## Configuration
In order to use this application, you must be a registered GroupMe user. Additionally, a client ID associated with 
your use of this software is required to obtain authentication through GroupMe's implementation of the OAuth 2.0 
Implicit Grant Flow.

#### Obtaining a Client ID
First, register as a developer using your GroupMe credentials at GroupMe's 
<a href="https://dev.groupme.com/">developer site</a>. Once registered, 
<a href="https://dev.groupme.com/applications/new">create a new application</a> with the following parameters:

<b>Note:</b> <i>You should keep track of your callback port number and client ID during this process</i>

<table>
    <tr>
        <th><b>Field</b></th>
        <th><b>Value</b></th>
        <th><b>Notes</b></th>
    </tr>
    <tr>
        <td><b>Application Name</b></td>
        <td>Cat Chat</td>
        <td>
            <i>If you are planning on using Cat Chat as is, leave the application name as "Cat Chat". Otherwise, use 
            your own application name.</i>
        </td>
    </tr>
    <tr>
        <td><b>Callback URL</b></td>
        <td>http://127.0.0.1:<span style="text-color:red">#####</span></td>
        <td>
            <i>The callback address should be the loopback address. You are free to specify any port number you'd like, 
            however be aware that the port number you use may be used by the system or another application. Choose 
            wisely.</i>
        </td>
    </tr>
    <tr>
        <td><b>Developer Name</b></td>
        <td style="text-color:red">Your Name</td>
        <td><i></i></td>
    </tr>
    <tr>
        <td><b>Developer Email</b></td>
        <td style="text-color:red">Your Email</td>
        <td><i></i></td>
    </tr>
    <tr>
        <td><b>Developer Phone Number</b></td>
        <td style="text-color:red">Your Phone Number</td>
        <td><i></i></td>
    </tr>
    <tr>
        <td><b>Developer Company</b></td>
        <td style="text-color:red">Your Company</td>
        <td><i></i></td>
    </tr>
    <tr>
        <td><b>Developer Address</b></td>
        <td style="text-color:red">Your Address</td>
        <td><i></i></td>
    </tr>
</table>
<i>* Red values in the table above can be replaced by values of your choice</i>

#### Creating a Properties File
Under <code>/src/main/resources/authentication/config</code> you will find a template properties file called 
<code>example.auth_properties.xml</code>. Open this file and replace the values inside of the XML tags with those you 
received while obtaining your client ID. The base authentication URL property should not be changed. Finally, rename 
this file to <code>auth_properties.xml</code> and you are ready to build Cat Chat!

## Build
