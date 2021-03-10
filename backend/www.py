from flask import Flask, jsonify, render_template, request
from flask_restful import Api,Resource,reqparse
from flask_cors import CORS
from Backend.APIs import handler1,handler2

# Initialize Flask
app = Flask(__name__,static_folder='../Frontend VUE',template_folder='../Frontend VUE',static_url_path="")
api = Api(app)

# Cross Domain
cors = CORS(app, resources={r"/*": {"origins": "*"}})

# Basic Route
@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'GET':
        return render_template('index.html')
    elif request.method == 'POST':
        key = request.args.get('key', '')
        return render_template('index.html', data=key)

# RESTful API Route
# handler1
api.add_resource(handler1.jsonAPI, '/api/<key>')

# handler2
api.add_resource(handler2.jsonAPI2, '/api/')