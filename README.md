# CatChat
Open source GroupMe chat client.

## Configuration
In order to use this application, you must be a registered GroupMe user. Additionally, a client ID associated with 
your use of this software is required to obtain authentication through GroupMe's implementation of the OAuth 2.0 
Implicit Grant Flow.

#### Obtaining a Client ID
First, register as a developer using your GroupMe credentials at GroupMe's 
<a href="https://dev.groupme.com/">developer site</a>. Once registered, <a href="">create a new application</a> with 
the following parameters:

<table border="1">
    <tr>
        <td style="font-weight:bold">Application Name</td>
        <td>Cat Chat</td>
    </tr>
    <tr>
        <td><b>Callback URL</b></td>
        <td>http://127.0.0.1:<i>51921</i></td>
    </tr>
    <tr>
        <td><b>Developer Name</b></td>
        <td><i>Your Name</i></td>
    </tr>
    <tr>
        <td><b>Developer Email</b></td>
        <td><i>Your Email</i></td>
    </tr>
    <tr>
        <td><b>Developer Phone Number</b></td>
        <td><i>Your Phone Number</i></td>
    </tr>
    <tr>
        <td><b>Developer Company</b></td>
        <td><i>Your Company</i></td>
    </tr>
    <tr>
        <td><b>Developer Address</b></td>
        <td><i>Your Address</i></td>
    </tr>
</table>
* Italic values in the table above can be replaced by values of your choice

#### Creating a Properties File
Under <code>/src/main/resources</code> you will find a template properties file called 
<code>example.auth.properties</code>. Open this file and replace the values inside of the XML tags with those you 
received while obtaining your client ID. The base authentication URL property should not be changed. Finally, rename 
this file to <code>auth.properties</code> and you are ready to build Cat Chat!

## Build
