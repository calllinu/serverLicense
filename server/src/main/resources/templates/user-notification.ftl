<html>
<head>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f9;
            color: #333;
        }
        .header {
            background-color: #1d3557;
            color: white;
            padding: 20px;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
        }
        .content {
            background: white;
            margin: 20px auto;
            padding: 20px;
            max-width: 600px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .content p {
            line-height: 1.6;
            margin: 10px 0;
        }
        .important-text {
            font-weight: bold;
            color: #1d3557;
        }
        .footer {
            margin-top: 20px;
            text-align: center;
            font-size: 14px;
            color: #888;
        }
    </style>
</head>
<body>
<div class="header">SafetyNet AI</div>
<div class="content">
    <p>Dear <span class="important-text">${fullName}</span>,</p>
    <p>${message}</p>
    <p>Thank you,<br><span class="important-text">SafetyNet AI Team</span></p>
</div>
<div class="footer">
    This is an automated message. Please do not reply.
</div>
</body>
</html>
